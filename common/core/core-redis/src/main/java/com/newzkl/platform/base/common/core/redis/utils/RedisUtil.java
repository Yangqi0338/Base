package com.newzkl.platform.base.common.core.redis.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 操作层门面（转发到各子工具类）
 *
 * @author sijiwang
 */
@Slf4j
public class RedisUtil extends RedisInternalUtil {

    public static <T> void set(String key, T value) {
        RedisStrUtil.set(key, value);
    }

    public static <T> void set(String key, T value, long timeout) {
        RedisStrUtil.set(key, value, timeout);
    }

    public static <T> void set(String key, T value, long timeout, TimeUnit unit) {
        RedisStrUtil.set(key, value, timeout, unit);
    }

    public static <T> T get(String key) {
        return RedisStrUtil.get(key);
    }

    public static <T> Map<String, T> get(List<String> keys) {
        return RedisStrUtil.get(keys);
    }

    public static void set(Map<String, String> keyValueMap) {
        RedisStrUtil.set(keyValueMap);
    }

    public static long incrBy(String key) {
        return RedisNumberUtil.incrBy(key);
    }

    public static long decrBy(String key) {
        return RedisNumberUtil.decrBy(key);
    }

    public static long numGet(String key) {
        return RedisNumberUtil.get(key);
    }

    public static long incrBy(String key, long delta) {
        return RedisNumberUtil.incrBy(key, delta);
    }

    public static <T> void hSet(String key, String field, T value) {
        RedisHashUtil.set(key, field, value);
    }

    public static <T> T hGet(String key, Object field) {
        return RedisHashUtil.get(key, field);
    }

    public static Map<String, Object> hGetAll(String key) {
        return RedisHashUtil.getAll(key);
    }

    public static long hDel(String key, String... fields) {
        return RedisHashUtil.del(key, fields);
    }

    public static long hIncrBy(String key, String field, long delta) {
        return RedisHashUtil.incrBy(key, field, delta);
    }

    public static long lPush(String key, Object... values) {
        return RedisListUtil.pushFirst(key, values);
    }

    public static long lRPush(String key, Object... values) {
        return RedisListUtil.pushLast(key, values);
    }

    public static List<Object> lRange(String key, long start, long end) {
        return RedisListUtil.range(key, start, end);
    }

    public static Object lPop(String key) {
        return RedisListUtil.popFirst(key);
    }

    public static boolean sAdd(String key, Object... values) {
        return RedisSetUtil.add(key, values);
    }

    public static Set<Object> sMembers(String key) {
        return RedisSetUtil.members(key);
    }

    public static boolean sContains(String key, Object value) {
        return RedisSetUtil.contains(key, value);
    }

    public static boolean sRem(String key, Object... values) {
        return RedisSetUtil.remove(key, values);
    }

    public static boolean zAdd(String key, Object value, double score) {
        return RedisZSetUtil.add(key, value, score);
    }

    public static Collection<Object> zRange(String key, int start, int end) {
        return RedisZSetUtil.range(key, start, end);
    }

    public static long del(String... keys) {
        return RedisKeyUtil.del(keys);
    }

    public static long batchDel(List<String> keys) {
        return RedisKeyUtil.batchDel(keys);
    }

    public static boolean expire(String key, long timeout, TimeUnit unit) {
        return RedisKeyUtil.expire(key, timeout, unit);
    }

    public static boolean expire(String key, long timeout) {
        return RedisKeyUtil.expire(key, timeout, TimeUnit.SECONDS);
    }

    public static boolean exists(String key) {
        return RedisKeyUtil.exists(key);
    }

    public static Iterable<String> getKeysByPattern(String pattern) {
        return RedisKeyUtil.getKeysByPattern(pattern);
    }

    public static long getRemainingTime(String key) {
        return RedisKeyUtil.getRemainingTime(key);
    }

    public static long getRemainingTime(String key, TimeUnit unit) {
        return RedisKeyUtil.getRemainingTime(key, unit);
    }

    public static Map<String, Object> getAllByKeyPattern(String pattern) {
        return RedisKeyUtil.getAllByKeyPattern(pattern);
    }
}
