package com.newzkl.platform.base.common.ddd.application.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 身份扩展点标记
 *
 * <p>标注于扩展点接口, 声明该接口按调用方身份多实现分发。运行期由分发器代理路由到唯一命中实现,
 * 各实现的身份条件由 {@code IdentityImpl} 声明, 启动期强制两两不相交。</p>
 *
 * @author KC
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentityExtension {
}
