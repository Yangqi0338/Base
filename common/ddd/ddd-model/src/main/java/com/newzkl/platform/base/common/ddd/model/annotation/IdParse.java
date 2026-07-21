package com.newzkl.platform.base.common.ddd.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典注解
 *
 * @author: dangzhenghui
 * @date: 2019年03月17日-下午9:37:16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdParse {

    String table();

    String column();

    String columnShow() default "";

    int columnShowType() default -1;
}
