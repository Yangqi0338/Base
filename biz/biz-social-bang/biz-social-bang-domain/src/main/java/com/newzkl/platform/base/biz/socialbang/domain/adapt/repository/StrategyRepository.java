package com.newzkl.platform.base.biz.socialbang.domain.adapt.repository;


import com.newzkl.platform.base.biz.socialbang.model.strategy.aggregates.StrategyRich;

/**
 * 策略数据仓库
 * @Author: niu
 * @Date: 2024/1/10 16:02
 */
public interface StrategyRepository {

    /**
     * 查询策略信息
     * @param strategyId 策略id
     * @return 策略信息
     */
    StrategyRich queryStrategyRich(Long strategyId);
}
