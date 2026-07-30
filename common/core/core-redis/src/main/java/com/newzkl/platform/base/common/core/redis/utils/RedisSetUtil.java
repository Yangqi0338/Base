package com.newzkl.platform.base.common.core.redis.utils;

import org.redisson.client.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSet;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

/**
 * Redis 集合（Set）类型操作工具类
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class RedisSetUtil extends RedisInternalUtil {

    public static <T> boolean add(String key, T... values) {
        try {
            RSet<T> set = redissonClient.getSet(key);
            return set.addAll(Arrays.asList(values));
        } catch (RedisException e) {
            throw new RuntimeException("Redis集合添加元素失败: key=" + key, e);
        }
    }

    public static <T> Set<T> members(String key) {
        try {
            RSet<T> set = redissonClient.getSet(key);
            return set.readAll();
        } catch (RedisException e) {
            throw new RuntimeException("Redis集合获取所有元素失败: key=" + key, e);
        }
    }

    public static <T> boolean contains(String key, T value) {
        try {
            RSet<T> set = redissonClient.getSet(key);
            return set.contains(value);
        } catch (RedisException e) {
            throw new RuntimeException("Redis集合判断元素存在失败: key=" + key, e);
        }
    }

    public static <T> boolean remove(String key, T... values) {
        try {
            RSet<T> set = redissonClient.getSet(key);
            return set.removeAll(Arrays.asList(values));
        } catch (RedisException e) {
            throw new RuntimeException("Redis集合移除元素失败: key=" + key, e);
        }
    }

    public static int size(String key) {
        try {
            RSet<Object> set = redissonClient.getSet(key);
            return set.size();
        } catch (RedisException e) {
            throw new RuntimeException("Redis获取集合元素数量失败: key=" + key, e);
        }
    }

    public static <T> T pop(String key) {
        try {
            RSet<T> set = redissonClient.getSet(key);
            return set.removeRandom();
        } catch (RedisException e) {
            throw new RuntimeException("Redis集合弹出元素失败: key=" + key, e);
        }
    }

    public static <T> Set<T> intersect(String key1, String key2) {
        try {
            RSet<T> set1 = redissonClient.getSet(key1);
            return set1.readIntersection(key2);
        } catch (RedisException e) {
            throw new RuntimeException("Redis集合交集操作失败: key1=" + key1 + ", key2=" + key2, e);
        }
    }

    public static <T> Set<T> union(String key1, String key2) {
        try {
            RSet<T> set1 = redissonClient.getSet(key1);
            return set1.readUnion(key2);
        } catch (RedisException e) {
            throw new RuntimeException("Redis集合并集操作失败: key1=" + key1 + ", key2=" + key2, e);
        }
    }
}
