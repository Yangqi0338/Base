package com.newzkl.platform.base.common.core.model.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSON 翻译注解
 *
 * <p>标在字段/getter 上, 序列化时在原字段旁追加同名 + "Desc" 的伴生字段,
 * 输出该字段值的可读文案: IEnum 取 getValue(), 普通枚举取 name(), 其他取 toString()。
 * 原字段保留不变。实际序列化器由 building-scm 的 BeanSerializerModifier 注入</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@JacksonAnnotationsInside
public @interface JsonTranslate {
}
