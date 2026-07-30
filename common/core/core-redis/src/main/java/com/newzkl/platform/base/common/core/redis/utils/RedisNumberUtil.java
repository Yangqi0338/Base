package com.newzkl.platform.base.common.core.redis.utils;

import org.redisson.client.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.springframework.stereotype.Component;

/**
 * Redis 原子数值类型操作工具类
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class RedisNumberUtil extends RedisInternalUtil {

    public static long incrBy(String key) {
        return incrBy(key, 1);
    }

    public static long decrBy(String key) {
        return decrBy(key, 1);
    }

    public static long get(String key) {
        try {
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            return atomicLong.get();
        } catch (RedisException e) {
            throw new RuntimeException("Redis获取原子长整型値失败: key=" + key, e);
        }
    }

    public static long incrBy(String key, long delta) {
        try {
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            return atomicLong.addAndGet(delta);
        } catch (RedisException e) {
            throw new RuntimeException("Redis原子递增失败: key=" + key, e);
        }
    }

    public static long decrBy(String key, long delta) {
        try {
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            return atomicLong.addAndGet(-delta);
        } catch (RedisException e) {
            throw new RuntimeException("Redis原子递少失败: key=" + key, e);
        }
    }

    public static void set(String key, long value) {
        try {
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            atomicLong.set(value);
        } catch (RedisException e) {
            throw new RuntimeException("Redis设置原子长整型値失败: key=" + key, e);
        }
    }
}
