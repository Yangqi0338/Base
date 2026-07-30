package com.newzkl.platform.base.common.core.redis.utils;

import org.redisson.client.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RDeque;
import org.redisson.api.RList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis 列表（List）类型操作工具类
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class RedisListUtil extends RedisInternalUtil {

    public static <T> long pushFirst(String key, T... values) {
        try {
            RDeque<T> deque = redissonClient.getDeque(key);
            for (T value : values) {
                deque.addFirst(value);
            }
            return deque.size();
        } catch (RedisException e) {
            throw new RuntimeException("Redis列表左侧添加失败: key=" + key, e);
        }
    }

    public static <T> long pushLast(String key, T... values) {
        try {
            RDeque<Object> deque = redissonClient.getDeque(key);
            for (T value : values) {
                deque.addLast(value);
            }
            return deque.size();
        } catch (RedisException e) {
            throw new RuntimeException("Redis列表右侧添加失败: key=" + key, e);
        }
    }

    public static <T> List<T> range(String key, long start, long end) {
        try {
            RList<T> list = redissonClient.getList(key);
            if (end == -1) {
                return new ArrayList<>(list);
            }
            return list.subList((int) start, (int) end + 1);
        } catch (RedisException e) {
            throw new RuntimeException("Redis列表获取范围元素失败: key=" + key, e);
        }
    }

    public static <T> T popFirst(String key) {
        try {
            RDeque<T> deque = redissonClient.getDeque(key);
            return deque.pollFirst();
        } catch (RedisException e) {
            throw new RuntimeException("Redis列表左侧弹出失败: key=" + key, e);
        }
    }

    public static <T> T popLast(String key) {
        try {
            RDeque<T> deque = redissonClient.getDeque(key);
            return deque.pollLast();
        } catch (RedisException e) {
            throw new RuntimeException("Redis列表右侧弹出失败: key=" + key, e);
        }
    }

    public static <T> long size(String key) {
        try {
            RList<T> list = redissonClient.getList(key);
            return list.size();
        } catch (RedisException e) {
            throw new RuntimeException("Redis获取列表长度失败: key=" + key, e);
        }
    }

    public static <T> T index(String key, int index) {
        try {
            RList<T> list = redissonClient.getList(key);
            return list.get(index);
        } catch (RedisException e) {
            throw new RuntimeException("Redis获取列表指定索引元素失败: key=" + key + ", index=" + index, e);
        }
    }

    public static <T> void trim(String key, int start, int end) {
        try {
            RList<T> list = redissonClient.getList(key);
            list.subList(start, end + 1);
        } catch (RedisException e) {
            throw new RuntimeException("Redis修剪列表失败: key=" + key, e);
        }
    }
}
