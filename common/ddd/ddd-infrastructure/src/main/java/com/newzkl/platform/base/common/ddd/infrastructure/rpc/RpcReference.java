package com.newzkl.platform.base.common.ddd.infrastructure.rpc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 跨域 RPC 引用注解
 *
 * <p>替代 {@code @DubboReference} 标注 infra 出站适配器对 facade 的依赖。单体部署下由
 * {@link RpcReferenceBeanPostProcessor} 按字段类型注入本地 provider bean; 将来拆微服务时
 * 仅需在处理器内切换为 Dubbo consumer 代理, 业务类零改动</p>
 *
 * @author KC
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    /**
     * 是否必须存在可注入的 provider
     *
     * <p>默认 false: 单体阶段部分域 facade 未迁全(如 biz-goods SPU 级), 缺 provider 时留空引用不阻塞启动;
     * 置 true 则缺 provider 直接抛异常</p>
     *
     * @return 是否强制要求 provider 存在
     */
    boolean required() default false;

    /**
     * 注入模式
     *
     * <p>AUTO 探测 dubbo 可用则先试远程再降级本地; DUBBO 优先远程失败降级本地; LOCAL 只本地 autowire</p>
     *
     * @return 注入模式, 默认 AUTO
     */
    Mode mode() default Mode.AUTO;

    /**
     * 注入模式枚举
     */
    enum Mode {

        /**
         * 自动探测: dubbo 可用先试远程, 否则本地 autowire
         */
        AUTO,

        /**
         * 优先 dubbo 远程, 远程无结果降级本地
         */
        DUBBO,

        /**
         * 强制本地 autowire, 不尝试远程
         */
        LOCAL
    }
}
