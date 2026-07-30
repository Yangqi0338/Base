package com.newzkl.platform.base.common.core.redis.utils;

import cn.hutool.core.util.ObjectUtil;
import org.redisson.client.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Redis 哈希（Hash）类型操作工具类
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class RedisHashUtil extends RedisInternalUtil {

    public static <T> void set(String key, Object field, T value) {
        if (ObjectUtil.isEmpty(field)) {
            return;
        }
        try {
            RMap<Object, T> map = redissonClient.getMap(key);
            map.put(field, value);
        } catch (RedisException e) {
            throw new RuntimeException("Redis哈希设置失败: key=" + key + ", field=" + field, e);
        }
    }

    public static <T> T get(String key, Object field) {
        if (ObjectUtil.isEmpty(field)) {
            return null;
        }
        try {
            RMap<Object, T> map = redissonClient.getMap(key);
            return map.get(field);
        } catch (RedisException e) {
            throw new RuntimeException("Redis哈希获取失败: key=" + key + ", field=" + field, e);
        }
    }

    public static Map<String, Object> getAll(String key) {
        try {
            RMap<String, Object> map = redissonClient.getMap(key);
            return new HashMap<>(map);
        } catch (RedisException e) {
            throw new RuntimeException("Redis哈希获取所有字段失败: key=" + key, e);
        }
    }

    public static long del(String key, Object... fields) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            return map.fastRemove(fields);
        } catch (RedisException e) {
            throw new RuntimeException("Redis哈希删除字段失败: key=" + key, e);
        }
    }

    public static long incrBy(String key, Object field, long delta) {
        try {
            RMap<Object, Long> map = redissonClient.getMap(key);
            Long result = map.addAndGet(field, delta);
            long finalResult = result != null ? result : delta;
            return Math.max(finalResult, 0L);
        } catch (RedisException e) {
            throw new RuntimeException("Redis哈希字段递增失败: key=" + key + ", field=" + field + ", delta=" + delta, e);
        }
    }

    public static boolean exists(String key, Object field) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            return map.containsKey(field);
        } catch (RedisException e) {
            throw new RuntimeException("Redis哈希判断字段存在失败: key=" + key + ", field=" + field, e);
        }
    }

    public static Set<String> keys(String key) {
        try {
            RMap<String, Object> map = redissonClient.getMap(key);
            return map.keySet();
        } catch (RedisException e) {
            throw new RuntimeException("Redis哈希获取所有字段名失败: key=" + key, e);
        }
    }

    public static <T> void setMap(String key, Map<Object, T> fieldMap) {
        try {
            RMap<Object, T> map = redissonClient.getMap(key);
            map.putAll(fieldMap);
        } catch (RedisException e) {
            throw new RuntimeException("Redis哈希批量设置失败: key=" + key, e);
        }
    }
}
