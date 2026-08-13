package com.newzkl.platform.base.common.ddd.infrastructure.rpc;

/**
 * dubbo 远程引用解析策略
 *
 * <p>{@link RpcReference} AUTO/DUBBO 模式下由 {@link RpcReferenceBeanPostProcessor} 调用, 尝试生成
 * dubbo consumer 代理。单体阶段默认 {@link NoopDubboReferenceResolver} 恒返 null, 使降级链落到本地
 * autowire; 微服务化时以真实 ReferenceConfig 实现覆盖此 bean</p>
 *
 * @author KC
 */
public interface DubboReferenceResolver {

    /**
     * 解析指定类型的 dubbo 远程代理
     *
     * @param type 引用接口类型
     * @param <T>  接口类型
     * @return 远程代理, 不可用返 null
     */
    <T> T resolve(Class<T> type);

    /**
     * dubbo 是否可用
     *
     * <p>true 时 AUTO 模式才尝试远程; classpath 探测 + 开关 + registry 有效性由真实实现判定</p>
     *
     * @return 是否可用
     */
    boolean available();
}
