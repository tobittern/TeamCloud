package com.euler.job.service;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.constant.CacheConstants;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.statistics.api.RemoteRechargeStatService;
import com.euler.system.api.domain.SysUserOnline;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RechargeStatJob {

    @DubboReference
    private RemoteRechargeStatService rechargeStatService;

    @XxlJob("fillDataToSigle")
    public void fillDataToSigle() {
        log.info("定时统计充值数据--执行开始");
        Date date = new Date();
        FillDataDto fillDataDto = new FillDataDto();
        fillDataDto.setBatchNo(DateUtil.format(date, DatePattern.PURE_DATETIME_MS_PATTERN))
            .setBeginTime(DateUtil.beginOfDay(date)).setEndTime(DateUtil.endOfDay(date));
        rechargeStatService.fillDataToSigle(fillDataDto);
        log.info("定时统计充值数据--执行结束");

    }

    @XxlJob("fillLastDataToSigle")
    public void fillLastDataToSigle() {
        log.info("定时统计充值数据--执行开始");
        Date date = DateUtil.yesterday();
        FillDataDto fillDataDto = new FillDataDto();
        fillDataDto.setBatchNo(DateUtil.format(date, DatePattern.PURE_DATETIME_MS_PATTERN))
            .setBeginTime(DateUtil.beginOfDay(date)).setEndTime(DateUtil.endOfDay(date));
        rechargeStatService.fillDataToSigle(fillDataDto);
        log.info("定时统计充值数据--执行结束");

    }


    @XxlJob("fillOnlineUser")
    public void fillOnlineUser() {
        String batchNo = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
        log.info("定时获取在线用户,batchNo:{}--执行开始", batchNo);
        getCurrentOnlineUser(batchNo);
        log.info("定时获取在线用户,batchNo:{}--执行结束", batchNo);

    }

    /**
     * 获取在线用户
     */
    private void getCurrentOnlineUser(String batchNo) {
        // 获取所有未过期的 token
        List<String> keys = StpUtil.searchTokenValue("", -1, 0);

        if (keys == null || keys.isEmpty()) {
            log.info("定时获取在线用户,batchNo:{}--获取在线用户token为空", batchNo);
            return;
        }
        log.info("定时获取在线用户--batchNo:{}，token总量：{}", batchNo, keys.size());

        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        Long currentTimeMillis = System.currentTimeMillis();
        int tmpNum = 0;
        Long activeTimeout = StpUtil.stpLogic.getConfig().getActivityTimeout();
        for (String key : keys) {
            if (tmpNum > 5) {
                tmpNum = 0;
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tmpNum++;

            String token = key.replace(CacheConstants.LOGIN_TOKEN_KEY, "");
            String keyLastActivityTime = StpUtil.stpLogic.splicingKeyLastActivityTime(token);
            String lastActivityTimeString = RedisUtils.getCacheObject(keyLastActivityTime);

            long lastActivityTime = Convert.toLong(lastActivityTimeString, currentTimeMillis);
            long apartSecond = (currentTimeMillis - lastActivityTime) / 1000L;
            long timeout = activeTimeout - apartSecond;
            // 如果已经过期则踢下线
            if (timeout < 0) {
                StpUtil.kickoutByTokenValue(token);
                continue;
            }
            if (apartSecond > 0 && apartSecond < 1800) {
                SysUserOnline userOnline = RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + token);

                if (userOnline != null && UserTypeEnum.SDK_USER.getUserType().equals(userOnline.getUserType())) {
                    log.info("获取在线用户,batchNo:{}，在线用户Id：{}，gameId：{}，channelId：{}，platform：{}，device：{}，packagecode：{}，最后活动时间：{}，timeout：{}", batchNo, userOnline.getUserId(), userOnline.getGameId(), userOnline.getChannelId(), userOnline.getPlatform(), userOnline.getDevice(), userOnline.getPackageCode(), DateTime.of(lastActivityTime), timeout);
                    if (userOnlineList.isEmpty()) {
                        userOnlineList.add(userOnline);
                        continue;
                    }
                    var first = userOnlineList.stream().filter(a -> a.getUserId().equals(userOnline.getUserId())).findFirst();
                    if (!first.isPresent())
                        userOnlineList.add(userOnline);
                } else {
                    if (userOnline != null)
                        log.info("获取在线用户,batchNo:{},userId:{}，非SDK用户", batchNo, userOnline.getUserId());
                    else
                        log.info("获取在线用户,batchNo:{},用户为空", batchNo);
                }
            }

        }

        if (userOnlineList != null && !userOnlineList.isEmpty()) {
            log.info("定时获取在线用户--batchNo:{}，在线用户总量：{}", batchNo, userOnlineList.size());
            //分批添加
            var lists = ListUtil.split(userOnlineList, 100);
            if (lists != null && !lists.isEmpty()) {
                for (var currentList : lists) {
                    try {
                        rechargeStatService.fillOnlineUser(currentList);
                    } catch (Exception e) {
                        log.error("定时获取在线用户--调用dubbo入库--异常,batchNo:{}", batchNo, e);
                    }
                }
            }


        }

    }


}
