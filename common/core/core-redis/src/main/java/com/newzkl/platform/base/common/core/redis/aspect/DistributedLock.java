package com.newzkl.platform.base.common.core.redis.aspect;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁注解
 *
 * <p>迁移说明: 原 {@code prefix()} 默认值引用业务枚举 {@code RedisEnum.Key}，
 * 通用层不承载业务枚举，已移除该属性 (实际切面未使用)。</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    /**
     * 锁的key，默认使用方法名作为key spEl
     *
     * @return key
     */
    @AliasFor("key")
    String value() default "";

    @AliasFor("value")
    String key() default "";

    /**
     * 获取锁的最大等待时间（单位：秒）
     *
     * @return 等待时间
     */
    long waitTime() default 3;

    /**
     * 上锁后有效时间（单位：秒）
     *
     * @return 租约时间
     */
    long leaseTime() default 15;

    String errorMsg() default "系统繁忙，请稍后重试";
}
