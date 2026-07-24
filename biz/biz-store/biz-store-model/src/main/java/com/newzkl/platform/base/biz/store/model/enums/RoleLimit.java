package com.newzkl.platform.base.biz.store.model.enums;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色访问限制标记注解。
 *
 * <p>标注在 controller 方法/类上, 声明允许访问的角色集合。</p>
 *
 * <p>TODO[auth-defer]: 强制拦截切面 (原 {@code com.zkl.scm.auth} AOP) 尚未迁入 Base,
 * 本注解当前为惰性标记, 仅保留安全语义与可检索性; 拦截逻辑待入口 starter 侧鉴权基建统一接入。</p>
 *
 * @author fang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleLimit {

    /**
     * 允许访问的角色集合。
     *
     * @return 角色数组, 默认空 (不限制)
     */
    RoleEnum.CompanyRole[] value() default {};
}
