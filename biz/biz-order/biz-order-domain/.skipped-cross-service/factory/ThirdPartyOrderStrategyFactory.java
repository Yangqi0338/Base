package com.newzkl.platform.base.biz.order.domain.factory;

import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.ThirdPartyOrderStrategy;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.impl.HuiDingHuoOrderStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方下单策略工厂
 */
public class ThirdPartyOrderStrategyFactory {
    private static final Map<Long, ThirdPartyOrderStrategy> STRATEGY_MAP = new HashMap<>();

    // 静态初始化策略
    static {
        STRATEGY_MAP.put(2L, new HuiDingHuoOrderStrategy()); // 惠订货供应商ID=2
    }

    /**
     * 根据供应商ID获取策略
     */
    public static ThirdPartyOrderStrategy getStrategy(Long supplierId) {
        if (supplierId == null) {
            return null;
        }
        return STRATEGY_MAP.get(supplierId);
    }

    /**
     * 动态注册新策略
     */
    public static void registerStrategy(Long supplierId, ThirdPartyOrderStrategy strategy) {
        if (supplierId != null && strategy != null) {
            STRATEGY_MAP.put(supplierId, strategy);
        }
    }
}