package com.newzkl.platform.base.common.ddd.action.auth;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能权限标记
 * <p>类标 → 父节点；方法标 → 子节点。
 * 类无注解但方法有 → 自动建父节点，名取 javadoc，缺失则取 simpleName。
 * @ext pid=0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FuncPermission {
    /**
     * 显示名。空则取 javadoc 或方法名/类名
     */
    String value() default "";

    /**
     * 唯一 code。空则用 ClassFQN#methodName 或 ClassFQN
     */
    String code() default "";

    /**
     * 所属端集合，用于给权限表 client 字段赋值，n 个 client 生成 n 条权限数据
     *
     * @return 端数组，默认平台端 ADMIN
     */
    AccountEnum.Client[] client() default {AccountEnum.Client.ADMIN};
}
