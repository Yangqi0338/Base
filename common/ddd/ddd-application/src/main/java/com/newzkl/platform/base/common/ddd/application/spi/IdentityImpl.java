package com.newzkl.platform.base.common.ddd.application.spi;

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
     * <p>取值为 {@code CompanyRole} 的 code 集合; 空数组表示 catch-all 兜底实现 (匹配所有未被其它实现命中的身份),
     * 每个扩展点至多允许一个 catch-all。</p>
     *
     * @return 身份 code 数组, 空数组为兜底
     */
    long[] value() default {};
}
