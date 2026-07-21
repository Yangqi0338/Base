package com.newzkl.platform.base.common.core.redis.utils;

import org.redisson.client.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 字符串（String）类型操作工具类。
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class RedisStrUtil extends RedisInternalUtil {

    public static <T> void set(String key, T value) {
        set(key, value, -1);
    }

    public static <T> void set(String key, T value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    public static <T> void set(String key, T value, long timeout, TimeUnit unit) {
        try {
            RBucket<T> bucket = redissonClient.getBucket(key);
            bucket.set(value, timeout, unit);
        } catch (RedisException e) {
            throw new RuntimeException("设置Redis键值对(带过期时间)失败: key=" + key, e);
        }
    }

    public static <T> T get(String key) {
        try {
            RBucket<T> bucket = redissonClient.getBucket(key);
            return bucket.get();
        } catch (RedisException e) {
            throw new RuntimeException("获取Redis值失败: key=" + key, e);
        }
    }

    public static <T> T getOrDefault(String key, T defaultValue) {
        try {
            RBucket<T> bucket = redissonClient.getBucket(key);
            T value = bucket.get();
            return value != null ? value : defaultValue;
        } catch (RedisException e) {
            throw new RuntimeException("获取Redis值失败: key=" + key, e);
        }
    }

    public static <T> Map<String, T> get(List<String> keys) {
        Map<String, T> resultMap = new HashMap<>(keys.size());
        for (String key : keys) {
            RBucket<T> bucket = redissonClient.getBucket(key);
            resultMap.put(key, bucket.get());
        }
        return resultMap;
    }

    public static <T> void set(Map<String, T> map) {
        map.forEach((key, value) -> {
            RBucket<T> bucket = redissonClient.getBucket(key);
            bucket.set(value);
        });
    }
}
