package com.newzkl.platform.base.common.ddd.model.auth;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录用户注入触发约束(类级)
 *
 * <p>标注在入参对象类上(通常在 {@code BaseReq} 上一处标注, 全体子类继承)。校验触发时由
 * 校验器扫描其 {@link OauthUserId}/{@code @OauthRole} 字段并回填当前用户ID/角色。</p>
 *
 * <p>本约束本身不判定合法性, 仅借校验生命周期承载注入; 注入失败(必填却未登录)时才判失败。</p>
 *
 * <p><b>validator 绑定方式</b>: {@code validatedBy} 留空, 校验器 {@code OauthUserInjectionValidator}
 * 位于 ddd-utils(依赖 SecurityUtils/RoleEnum), 经该模块 {@code META-INF/validation.xml}
 * 声明式绑定。如此 ddd-model 无需反依赖 ddd-utils。</p>
 *
 * @author KC
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface OauthUserInjection {

    /**
     * 校验失败提示
     *
     * @return 提示文案
     */
    String message() default "用户未登录";

    /**
     * 分组
     *
     * @return 分组数组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     *
     * @return 负载数组
     */
    Class<? extends Payload>[] payload() default {};
}
