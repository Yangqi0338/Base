package com.newzkl.platform.base.common.ddd.application.spi;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 身份实现声明
 *
 * <p>标注于 {@code IdentityExtension} 扩展点的实现类, 声明其命中的身份条件。元注解 {@code Component}
 * 使实现类自动注册为 Spring bean, 供分发器收集。同一扩展点下所有实现的条件集必须两两不相交, 否则启动失败。</p>
 *
 * @author KC
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface IdentityImpl {

    /**
     * 命中的身份条件集
     *
     * <p>不命名为 {@code value}: 本注解元标注 {@code Component}, Spring {@code AnnotationBeanNameGenerator}
     * 会读取 {@code value} 属性当作 bean 名 (要求 String), 而此处为枚举数组, 会导致启动期类型不匹配崩溃。</p>
     *
     * @return 身份 code 数组, 空数组为兜底
     */
    AccountEnum.Identity[] identities() default {};
}
