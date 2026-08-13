package com.newzkl.platform.base.common.ddd.infrastructure.rpc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * dubbo 解析器默认空实现
 *
 * <p>单体阶段占位: 恒返 null 且 {@link #available()} 为 false, 使全部 {@link RpcReference} 走本地
 * autowire。用 {@link ConditionalOnMissingBean} 注册, 微服务化提供真实 resolver bean 时自动让位</p>
 *
 * @author KC
 */
@Component
@ConditionalOnMissingBean(DubboReferenceResolver.class)
public class NoopDubboReferenceResolver implements DubboReferenceResolver {

    /**
     * 恒返 null, 降级链落本地
     *
     * @param type 引用接口类型
     * @param <T>  接口类型
     * @return 恒 null
     */
    @Override
    public <T> T resolve(Class<T> type) {
        return null;
    }

    /**
     * 恒不可用
     *
     * @return 恒 false
     */
    @Override
    public boolean available() {
        return false;
    }
}
