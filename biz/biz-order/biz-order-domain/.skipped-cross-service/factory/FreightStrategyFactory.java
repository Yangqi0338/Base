package com.newzkl.platform.base.biz.order.domain.factory;

import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.FreightCalculateStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 运费计算策略工厂
 * @author sijiwang
 */
@Component
@RequiredArgsConstructor
public class FreightStrategyFactory {

    /** 缓存策略实例 */
    private final Map<Long, FreightCalculateStrategy> strategyCache = new ConcurrentHashMap<>();

    /** 注入所有策略实现 */
    private final List<FreightCalculateStrategy> freightCalculateStrategies;

    /**
     * 初始化策略缓存
     */
    public void init() {
        for (FreightCalculateStrategy strategy : freightCalculateStrategies) {
            strategyCache.put(strategy.getTemplateId(), strategy);
        }
    }

    /**
     * 获取运费计算策略
     * @param templateId 运费模板ID
     * @return 运费计算策略
     */
    public FreightCalculateStrategy getStrategy(Long templateId) {
        // 优先匹配指定模板ID的策略
        FreightCalculateStrategy strategy = strategyCache.get(templateId);
        if (strategy != null) {
            return strategy;
        }
        // 匹配通用模板策略（自定义模板）
        return strategyCache.get(-1L);
    }
}