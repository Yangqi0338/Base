package com.newzkl.platform.base.common.core.redis.utils;


import org.redisson.client.RedisException;

import java.util.concurrent.TimeUnit;

/**
 * 忘记密码签名验签场景专属Redis工具类。
 *
 * @author sijiwang
 */
public class ResetPwdRedisUtil {

    public final static Integer EXPIRE_MINUTES = 5;
    /**
     * nonce防重放key前缀
     */
    private static final String NONCE_KEY_PREFIX = "reset:pwd:nonce:";
    /**
     * 接口限流key前缀
     */
    private static final String RATE_LIMIT_KEY_PREFIX = "reset:pwd:rate:";

    /**
     * 标记nonce已使用（防重放攻击）。
     *
     * @param nonce         随机串
     * @param expireMinutes 过期时间（分钟）
     */
    public static void markNonceUsed(String nonce, int expireMinutes) {
        if (nonce == null || nonce.trim().isEmpty()) {
            throw new IllegalArgumentException("nonce不能为空");
        }
        String key = NONCE_KEY_PREFIX + nonce;
        try {
            RedisUtil.set(key, "1", expireMinutes, TimeUnit.MINUTES);
        } catch (RedisException e) {
            throw new RuntimeException("忘记密码场景-标记nonce已使用失败: nonce=" + nonce, e);
        }
    }

    /**
     * 检查nonce是否已使用（防重放）。
     *
     * @param nonce 随机串
     * @return true=已使用/已过期，false=未使用
     */
    public static boolean isNonceUsed(String nonce) {
        if (nonce == null || nonce.trim().isEmpty()) {
            throw new IllegalArgumentException("nonce不能为空");
        }
        String key = NONCE_KEY_PREFIX + nonce;
        try {
            return RedisUtil.exists(key);
        } catch (RedisException e) {
            throw new RuntimeException("忘记密码场景-检查nonce是否已使用失败: nonce=" + nonce, e);
        }
    }

    /**
     * 忘记密码接口限流。
     *
     * @param uniqueKey     限流标识
     * @param limit         限制次数
     * @param expireSeconds 限流时间（秒）
     * @return true=超出限制，false=未超出
     */
    public static boolean isOverRateLimit(String uniqueKey, int limit, int expireSeconds) {
        if (uniqueKey == null || uniqueKey.trim().isEmpty()) {
            throw new IllegalArgumentException("限流标识uniqueKey不能为空");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("限流次数limit必须大于0");
        }
        if (expireSeconds <= 0) {
            throw new IllegalArgumentException("限流时间expireSeconds必须大于0");
        }

        String key = RATE_LIMIT_KEY_PREFIX + uniqueKey;
        try {
            long currentCount = RedisUtil.incrBy(key);

            if (currentCount == 1) {
                RedisUtil.expire(key, expireSeconds, TimeUnit.SECONDS);
            }

            return currentCount > limit;
        } catch (RedisException e) {
            throw new RuntimeException("忘记密码场景-接口限流检查失败: uniqueKey=" + uniqueKey, e);
        }
    }
}
