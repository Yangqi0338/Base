package com.newzkl.platform.base.common.ddd.domain;


/**
 * 策略处理器，不可为空
 */
@FunctionalInterface
public interface StrategyProcessor {
    /**
     * 什么类型的策略处理器
     */
    boolean supports(Object type);
}