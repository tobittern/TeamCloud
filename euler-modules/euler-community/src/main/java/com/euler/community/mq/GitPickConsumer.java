package com.euler.community.mq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.LockHelper;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.community.constant.GitBagConstant;
import com.euler.community.domain.entity.GiftBag;
import com.euler.community.domain.entity.GiftBagCdk;
import com.euler.community.mapper.GiftBagCdkMapper;
import com.euler.community.mapper.GiftBagMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 礼包领取消息队列
 */
@Component
@Slf4j
public class GitPickConsumer {

    @Resource
    private GiftBagMapper baseMapper;

    @Resource
    private GiftBagCdkMapper giftBagCdkMapper;
    @Autowired
    private LockHelper lockHelper;


    private String getMsgId(Message message) {
        if (message == null || message.getMessageProperties() == null
            || message.getMessageProperties().getHeaders() == null
            || message.getMessageProperties().getHeaders().get("spring_returned_message_correlation") == null) {
            return "未知msgId";
        } else {
            return message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        }

    }

    @RabbitListener(queues = "${webconfig.rabbitmq.bagPickQueue}")
    public void gitPickQueue(String msg, Message messages) {
        // 自定义排查的msgId
        String msgId = getMsgId(messages);
        log.info("礼包领取消费====>,msgId:{}, msg:{}", msgId, msg);
        HashMap hashMap = JsonUtils.parseObject(msg, HashMap.class);
        Long giftBagId = hashMap == null ? null : (hashMap.get("giftBagId") == null ? null : Long.parseLong(hashMap.get("giftBagId").toString()));
        Long memberId = hashMap == null ? null : (hashMap.get("memberId") == null ? null : Long.parseLong(hashMap.get("memberId").toString()));
        if (giftBagId == null || memberId == null) {
            log.info("数据异常，不做处理");
            return;
        }
        String key = StringUtils.format("{}{}{}", Constants.BASE_KEY, GitBagConstant.GIT_BAG_LOCK_PREFIX, giftBagId);
        var lock = lockHelper.lock(key);
        try {

            if (null != lock) {
                LambdaQueryWrapper<GiftBag> lqw = Wrappers.lambdaQuery();
                lqw.eq(GiftBag::getStatus, 1);//已上架的
                Date currentDate = new Date();
                lqw.le(GiftBag::getStartTime, currentDate);//开始时间小于等于当前时间
                lqw.ge(GiftBag::getEndTime, currentDate);//结束时间大于等于当前时间
                lqw.eq(GiftBag::getId, giftBagId);//礼包id
                lqw.eq(GiftBag::getDelFlag, 0);//未删除
                List<GiftBag> giftBags = baseMapper.selectList(lqw);
                if (giftBags.isEmpty()) {
                    log.info("礼包已过期或已下架,礼包id:{},当前领取人id:{}", giftBagId, memberId);
                    return;
                }
                GiftBag giftBag = giftBags.get(0);
                //查询改礼包是不是已经被该用户获取
                LambdaQueryWrapper<GiftBagCdk> cdkLqw = Wrappers.lambdaQuery();
                cdkLqw.eq(GiftBagCdk::getGiftBagId, giftBagId);
                cdkLqw.eq(GiftBagCdk::getMemberId, memberId);
                List<GiftBagCdk> giftBagCdkList = giftBagCdkMapper.selectList(cdkLqw);
                //说明已经领取过了
                if (!giftBagCdkList.isEmpty()) {
                    log.info("你已经领取过该礼包,礼包id:{},激活码:{},当前领取人id:{}", giftBagId, giftBagCdkList.get(0).getCode(), memberId);
                    return;
                }
                //判断礼包是否已经被领取完了
                if (giftBag.getDrawNum().equals(giftBag.getTotalNum())) {
                    log.info("礼包已被领完,礼包id++>:{},当前领取人id:{}", giftBagId, memberId);
                    return;
                }
                //将未领取的礼包分给该用户，默认取出第一个
                LambdaQueryWrapper<GiftBagCdk> noCdkLqw = Wrappers.lambdaQuery();
                noCdkLqw.eq(GiftBagCdk::getGiftBagId, giftBagId);
                noCdkLqw.isNull(GiftBagCdk::getMemberId);//用户id为null
                List<GiftBagCdk> noGiftBagCdkList = giftBagCdkMapper.selectList(noCdkLqw);
                if (noGiftBagCdkList.isEmpty()) {
                    log.info("礼包已被领完,礼包id==>:{},当前领取人id:{}", giftBagId, memberId);
                    return;
                }
                GiftBagCdk noGiftBagCdk = noGiftBagCdkList.get(0);
                noGiftBagCdk.setMemberId(memberId);//更新用户id
                noGiftBagCdk.setReceiveTime(new Date());
                giftBagCdkMapper.updateById(noGiftBagCdk);
                //更新礼包的已领取数目
                Integer drawNum = giftBag.getDrawNum() + 1;
                giftBag.setDrawNum(drawNum);
                baseMapper.updateById(giftBag);
                //更新缓存中礼包信息
                //缓存key=礼包id,游戏id
                String cashKey = StringUtils.format("{}{}{}", Constants.BASE_KEY, GitBagConstant.GIT_BAG_PREFIX, giftBagId);
                //缓存value=总数,已领数目
                String value = giftBag.getTotalNum() + "," + drawNum;
                if (RedisUtils.hasKey(cashKey)) {
                    RedisUtils.deleteObject(cashKey);
                }
                Date currentTime = new Date();//当前时间
                Date endTime = giftBag.getEndTime();//结束时间
                long time = endTime.getTime() - currentTime.getTime();
                RedisUtils.setCacheObject(cashKey, value, Duration.ofMillis(time));
                //将领取过该礼包的用户锁定直到礼包时间结束
                String lockKey = StringUtils.format("{}{}{}:{}", Constants.BASE_KEY, GitBagConstant.GIT_BAG_LOCK_USER_PREFIX, giftBagId, memberId);
                if (RedisUtils.hasKey(lockKey)) {
                    RedisUtils.deleteObject(lockKey);
                }
                RedisUtils.setCacheObject(lockKey, lockKey, Duration.ofMillis(time));
            } else {
                log.info("分布式锁获取失败,礼包id:{},当前领取人id:{}", giftBagId, memberId);
            }

        } catch (Exception e) {
            log.info("领取礼包异常:{},礼包id:{},当前领取人id:{}", e.getMessage(), giftBagId, memberId);
        } finally {
            lockHelper.unLock(lock);
        }
    }
}
