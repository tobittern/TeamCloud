package com.euler.community.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.domain.entity.UserBehavior;
import com.euler.community.mapper.UserBehaviorMapper;
import com.euler.community.service.IUserBehaviorService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.ScoreSystemBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户行为记录Service业务层处理
 *
 * @author euler
 * @date 2022-07-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements IUserBehaviorService {

    private final UserBehaviorMapper baseMapper;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private CommonCommunityConfig commonCommunityConfig;
    @Autowired
    private LockTemplate lockTemplate;

    /**
     * 检测用户行为是否合法
     *
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Boolean checkUserBehaviorLegitimate(Long userId, Integer type, Long relationId) {
        if (userId == null || relationId == 0L) {
            return false;
        }
        String lockKey = Constants.BASE_KEY + "LOCK:APP_GIVE_USER_SCORE:" + userId + ":" + type;
        final LockInfo lockInfo = lockTemplate.lock(lockKey, 30000L, 5000L, RedissonLockExecutor.class);
        try {
            if (null == lockInfo) {
                return false;
            }
            String dateTime = DateUtil.format(new Date(), "yyyy-MM-dd");
            String key = Constants.COMMUNITY_KEY + "integral:" + userId + ":" + type + ":" + dateTime;
            // 首先判断
            List<Long> cacheList = RedisUtils.getCacheList(key);
            Boolean isOpen = false;
            if (cacheList.size() <= 0) {
                // 第一次进行操作
                List<Long> relationIds = new ArrayList<>();
                relationIds.add(relationId);
                RedisUtils.setCacheList(key, relationIds, Duration.ofDays(2));
                isOpen = true;
            } else {
                // 缓存存在 但是我们需要判断里面的动态id是否已经存在过
                if (cacheList.contains(relationId)) {
                    return false;
                }
                int aLong = cacheList.size();
                if ((type == 1 && aLong >= 3)
                    || (type == 2 && aLong >= 5)
                    || (type == 3 && aLong >= 5)) {
                    return false;
                }
                List<Long> insertRelationId = new ArrayList<>();
                insertRelationId.add(relationId);
                RedisUtils.setCacheList(key, insertRelationId, Duration.ofDays(2));
                isOpen = true;
            }
            // 判断是否需要入库
            if (isOpen) {
                boolean exist = baseMapper.exists(new LambdaQueryWrapper<UserBehavior>()
                    .eq(UserBehavior::getMemberId, userId)
                    .eq(UserBehavior::getType, type)
                    .eq(UserBehavior::getRelationId, relationId)
                    .eq(UserBehavior::getTime, dateTime));
                if (!exist) {
                    UserBehavior userBehavior = new UserBehavior();
                    userBehavior.setMemberId(userId);
                    userBehavior.setType(type);
                    userBehavior.setRelationId(relationId);
                    userBehavior.setTime(dateTime);
                    userBehavior.setUpdateTime(new Date());
                    baseMapper.insert(userBehavior);
                    // 增加积分操作
                    ScoreSystemBo scoreSystemBo = new ScoreSystemBo();
                    scoreSystemBo.setUserId(userId);
                    if (type == 1) {
                        scoreSystemBo.setScore(10L);
                        scoreSystemBo.setDesc("发动态");
                    } else if (type == 2) {
                        scoreSystemBo.setScore(1L);
                        scoreSystemBo.setDesc("点赞");
                    } else if (type == 3) {
                        scoreSystemBo.setScore(5L);
                        scoreSystemBo.setDesc("评论");
                    }
                    remoteMemberService.calculateScore(scoreSystemBo);
                    return true;
                }
            }
        } catch (Exception exception) {
            log.error("用户给积分的时候报错", exception);
        } finally {
            //释放锁
            lockTemplate.releaseLock(lockInfo);
        }
        return false;
    }


    /**
     * 格式用户的头像信息
     *
     * @param sex
     * @param avatar
     * @return
     */
    @Override
    public String getAvatar(String sex, String avatar) {
        return remoteMemberService.getAvatar(sex, avatar);
    }
}
