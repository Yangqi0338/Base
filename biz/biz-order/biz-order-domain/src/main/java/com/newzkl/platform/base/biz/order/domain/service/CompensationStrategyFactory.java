// 文件路径: com/zkl/scm/sale/domain/order/service/impl/compensator/CompensationStrategyFactory.java

package com.newzkl.platform.base.biz.order.domain.service;


import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 补偿策略工厂
 * @author sijiwang
 */
@Component
@RequiredArgsConstructor
public class CompensationStrategyFactory implements InitializingBean {

    // Spring会自动注入所有实现了Compensator接口的Bean
    private final List<Compensator> compensators;

    private static final Map<PlatformTypeEnum, Compensator> STRATEGY_MAP = new HashMap<>();

    /**
     * Spring Bean初始化时，将所有Compensator注册到STRATEGY_MAP中
     *
     * <p>迁移: 原 {@code @PostConstruct}(jakarta.annotation 不在 domain classpath) 改 InitializingBean
     */
    @Override
    public void afterPropertiesSet() {
        for (Compensator compensator : compensators) {
            STRATEGY_MAP.put(compensator.getPlatformType(), compensator);
        }
    }

    /**
     * 根据平台类型获取对应的补偿策略
     * @param platformType 平台类型
     * @return 补偿策略
     */
    public static Compensator getStrategy(PlatformTypeEnum platformType) {
        Compensator strategy = STRATEGY_MAP.get(platformType);
        if (strategy == null) {
            throw new IllegalArgumentException("未找到平台 " + platformType + " 对应的补偿策略");
        }
        return strategy;
    }
}