package com.newzkl.platform.base.common.ddd.domain;


/**
 * 策略处理器，可为空
 */
@FunctionalInterface
public interface StrategyNullableProcessor {
    /**
     * 什么类型的策略处理器
     */
    boolean supports(Object type);
}