package com.newzkl.platform.base.common.core.redis.lock.impl;

import com.newzkl.platform.base.common.core.redis.lock.DistributedLocker;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Redis 分布式锁工具类（基于 {@code DistributedLocker} 封装）
 *
 * @author yangzhilong
 */
public class RedissonLockUtil {

    private static DistributedLocker redissLock;

    public static void setLocker(DistributedLocker locker) {
        redissLock = locker;
    }

    public static RLock lock(String lockKey) {
        return redissLock.lock(lockKey);
    }

    public static void unlock(String lockKey) {
        redissLock.unlock(lockKey);
    }

    public static void unlock(RLock lock) {
        redissLock.unlock(lock);
    }

    public static RLock lock(String lockKey, int timeout) {
        return redissLock.lock(lockKey, timeout);
    }

    public static RLock lock(String lockKey, TimeUnit unit, int timeout) {
        return redissLock.lock(lockKey, unit, timeout);
    }

    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        return redissLock.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
    }

    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        return redissLock.tryLock(lockKey, unit, waitTime, leaseTime);
    }
}
