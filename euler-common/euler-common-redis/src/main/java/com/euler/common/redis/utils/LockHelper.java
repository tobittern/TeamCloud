package com.euler.common.redis.utils;

import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.baomidou.lock.spring.boot.autoconfigure.Lock4jProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LockHelper {

    @Autowired
    private LockTemplate lockTemplate;
    @Autowired
    private Lock4jProperties properties;

    /**
     * 加锁
     *
     * @param key
     * @return
     */
    public LockInfo lock(String key) {
        try {
            LockInfo lockInfo = lockTemplate.lock(key, properties.getExpire(), properties.getAcquireTimeout(), RedissonLockExecutor.class);
            return lockInfo;

        } catch (Exception e) {
            log.error("获取分布式锁失败，key：{}", key, e);
            return null;
        }
    }

    /**
     * 加锁，带重试功能
     *
     * @param key      加锁key
     * @param times    重试次数
     * @param interval 重试间隔
     * @return
     */
    public LockInfo lock(String key, int times, long interval) {
        try {
            LockInfo lockInfo = lockTemplate.lock(key, properties.getExpire(), properties.getAcquireTimeout(), RedissonLockExecutor.class);
            if (times > 0 && lockInfo == null) {
                times = times - 1;
                Thread.sleep(interval);
                return lock(key,times,interval);
            }
            return lockInfo;

        } catch (Exception e) {
            log.error("获取分布式锁失败，key：{}", key, e);
            return null;
        }
    }


    /**
     * 释放锁
     *
     * @param lockInfo
     * @return
     */
    public Boolean unLock(LockInfo lockInfo) {
        try {
            if (lockInfo!=null)
            return lockTemplate.releaseLock(lockInfo);
            return  true;
        } catch (Exception e) {
            log.error("释放分布式锁失败", e);
            return false;
        }
    }
}
