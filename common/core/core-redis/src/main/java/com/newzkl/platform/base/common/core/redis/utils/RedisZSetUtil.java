package com.newzkl.platform.base.common.core.redis.utils;

import org.redisson.client.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Redis 有序集合（ZSet）类型操作工具类。
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class RedisZSetUtil extends RedisInternalUtil {

    public static boolean add(String key, Object value, double score) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.add(score, value);
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合添加元素失败: key=" + key, e);
        }
    }

    public static Collection<Object> range(String key, int start, int end) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.valueRange(start, end);
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合获取范围元素失败: key=" + key, e);
        }
    }

    public static Collection<Object> rangeReversed(String key, int start, int end) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.valueRangeReversed(start, end);
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合降序获取范围元素失败: key=" + key, e);
        }
    }

    public static Double score(String key, Object value) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.getScore(value);
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合获取分数失败: key=" + key, e);
        }
    }

    public static Integer rank(String key, Object value) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.rank(value);
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合获取排名失败: key=" + key, e);
        }
    }

    public static Integer rankReversed(String key, Object value) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.revRank(value);
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合获取降序排名失败: key=" + key, e);
        }
    }

    public static boolean remove(String key, Object... values) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.removeAll(java.util.Arrays.asList(values));
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合移除元素失败: key=" + key, e);
        }
    }

    public static int size(String key) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.size();
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合获取元素数量失败: key=" + key, e);
        }
    }

    public static double incrScore(String key, Object value, double delta) {
        try {
            RScoredSortedSet<Object> zSet = redissonClient.getScoredSortedSet(key);
            return zSet.addScore(value, delta);
        } catch (RedisException e) {
            throw new RuntimeException("Redis有序集合分数递增失败: key=" + key, e);
        }
    }
}
