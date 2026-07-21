package com.newzkl.platform.base.common.core.redis.utils;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Redis 工具基类，持有静态 {@link RedissonClient}。
 *
 * @author sijiwang
 */
abstract class RedisInternalUtil {

    public static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RedisInternalUtil.redissonClient = redissonClient;
    }
}
