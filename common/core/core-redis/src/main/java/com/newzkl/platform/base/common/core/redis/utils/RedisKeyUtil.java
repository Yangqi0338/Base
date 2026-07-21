package com.newzkl.platform.base.common.core.redis.utils;

import org.redisson.client.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 通用键（Key）操作工具类。
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class RedisKeyUtil extends RedisInternalUtil {

    public static long del(String... keys) {
        try {
            return redissonClient.getKeys().delete(keys);
        } catch (RedisException e) {
            throw new RuntimeException("Redis删除键失败", e);
        }
    }

    public static long batchDel(List<String> keys) {
        try {
            return redissonClient.getKeys().delete(keys.toArray(new String[0]));
        } catch (RedisException e) {
            throw new RuntimeException("Redis批量删除键失败, keys数量=" + keys.size(), e);
        }
    }

    public static boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            return redissonClient.getBucket(key).expire(timeout, unit);
        } catch (RedisException e) {
            throw new RuntimeException("Redis设置过期时间失败: key=" + key, e);
        }
    }

    public static boolean exists(String key) {
        try {
            return redissonClient.getKeys().countExists(key) > 0;
        } catch (RedisException e) {
            throw new RuntimeException("Redis检查键是否存在失败: key=" + key, e);
        }
    }

    public static Iterable<String> getKeysByPattern(String pattern) {
        try {
            return redissonClient.getKeys().getKeysByPattern(pattern);
        } catch (RedisException e) {
            throw new RuntimeException("Redis获取匹配keys失败: pattern=" + pattern, e);
        }
    }

    public static long getRemainingTime(String key) {
        try {
            RBucket<Object> bucket = redissonClient.getBucket(key);
            return bucket.remainTimeToLive();
        } catch (RedisException e) {
            throw new RuntimeException("Redis获取剩余过期时间失败: key=" + key, e);
        }
    }

    public static long getRemainingTime(String key, TimeUnit unit) {
        long remainingMs = getRemainingTime(key);
        if (remainingMs <= 0) {
            return remainingMs;
        }
        return unit.convert(remainingMs, TimeUnit.MILLISECONDS);
    }

    public static boolean persist(String key) {
        try {
            return redissonClient.getBucket(key).clearExpire();
        } catch (RedisException e) {
            throw new RuntimeException("Redis移除过期时间失败: key=" + key, e);
        }
    }

    public static String type(String key) {
        try {
            RType keyType = redissonClient.getKeys().getType(key);
            if (keyType == null) {
                return "UNKNOWN";
            }
            switch (keyType) {
                case OBJECT:
                    return "STRING";
                case MAP:
                    return "HASH";
                case LIST:
                    return "LIST";
                case SET:
                    return "SET";
                case ZSET:
                    return "ZSET";
                default:
                    return keyType.name();
            }
        } catch (RedisException e) {
            throw new RuntimeException("Redis获取键类型失败: key=" + key, e);
        }
    }

    public static void rename(String key, String newKey) {
        try {
            redissonClient.getKeys().rename(key, newKey);
        } catch (RedisException e) {
            throw new RuntimeException("Redis重命名键失败: key=" + key + " -> " + newKey, e);
        }
    }

    public static Map<String, Object> getAllByKeyPattern(String pattern) {
        Map<String, Object> resultMap = new HashMap<>();
        if (pattern == null || pattern.trim().isEmpty()) {
            log.warn("Redis查询pattern为空，返回空结果");
            return resultMap;
        }
        try {
            Iterable<String> matchKeys = redissonClient.getKeys().getKeysByPattern(pattern);
            if (matchKeys == null) {
                log.info("未匹配到任何Redis key，pattern:{}", pattern);
                return resultMap;
            }
            for (String key : matchKeys) {
                try {
                    RType keyType = redissonClient.getKeys().getType(key);
                    if (keyType == null) {
                        log.debug("Redis key[{}]不存在或类型未知，跳过", key);
                        continue;
                    }
                    Object value = readRedisValueByType(key, keyType);
                    resultMap.put(key, value);
                } catch (Exception e) {
                    log.error("读取Redis key[{}]的内容失败", key, e);
                    resultMap.put(key, null);
                }
            }
            log.info("Redis前缀查询完成，pattern:{}, 匹配到{}个key", pattern, resultMap.size());
        } catch (Exception e) {
            throw new RuntimeException("Redis根据前缀查询所有类型key失败: pattern=" + pattern, e);
        }
        return resultMap;
    }

    private static Object readRedisValueByType(String key, RType keyType) {
        switch (keyType) {
            case OBJECT:
                RBucket<String> stringBucket = redissonClient.getBucket(key);
                return stringBucket.get();
            case SET:
                RSet<String> set = redissonClient.getSet(key);
                return new ArrayList<>(set.readAll());
            case MAP:
                RMap<String, String> hashMap = redissonClient.getMap(key);
                return hashMap.readAllMap();
            case LIST:
                RList<String> list = redissonClient.getList(key);
                return list.readAll();
            case ZSET:
                RScoredSortedSet<String> zSet = redissonClient.getScoredSortedSet(key);
                return new ArrayList<>(zSet.readAll());
            default:
                log.warn("不支持的Redis数据类型（RType）：{}，key：{}", keyType.name(), key);
                return null;
        }
    }
}
