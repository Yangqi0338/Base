package com.newzkl.platform.base.common.ddd.action.auth;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色访问限制注解
 *
 * <p>标注在 controller 类或方法上, 声明允许访问的角色集合。由 {@link RoleLimitAspect} 拦截,
 * 取当前登录者角色比对, 不在允许集合内抛权限异常。空 {@code value} 表示不限制。</p>
 *
 * @author fang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleLimit {

    /**
     * 允许访问的角色集合
     *
     * @return 角色数组, 默认空 (不限制)
     */
    RoleEnum.CompanyRole[] value() default {};

    /**
     * 允许访问的端集合
     *
     * @return 角色数组, 默认空 (不限制)
     */
    CommonEnum.Client[] client() default {};
}
