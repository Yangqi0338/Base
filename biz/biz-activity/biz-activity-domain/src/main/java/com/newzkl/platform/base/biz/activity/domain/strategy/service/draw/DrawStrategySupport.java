package com.newzkl.platform.base.biz.activity.domain.strategy.service.draw;


import com.newzkl.platform.base.biz.activity.model.strategy.aggregates.StrategyRich;
import com.newzkl.platform.base.biz.activity.domain.adapt.repository.StrategyRepository;

import jakarta.annotation.Resource;

/**
 * 抽取策略数据服务支持
 * @Author: niu
 * @Date: 2024/1/10 11:20
 */
public class DrawStrategySupport extends DrawConfig {

    @Resource
    protected StrategyRepository strategyRepository;

    /**
     * 查询策略配置信息
     * @param strategyId 策略ID
     * @return 策略配置信息
     */
    protected StrategyRich queryStrategyRich(Long strategyId){
        return strategyRepository.queryStrategyRich(strategyId);
    }
}
