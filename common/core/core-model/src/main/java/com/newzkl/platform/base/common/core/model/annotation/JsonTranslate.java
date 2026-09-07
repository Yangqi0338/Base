package com.newzkl.platform.base.common.core.model.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSON 翻译注解
 *
 * <p>标在字段/getter 上, 序列化时在原字段旁追加同名 + "Desc" 的伴生字段, 输出该字段值的可读文案, 原字段保留不变</p>
 * <p>取值分两段: 先按 index 从多语义描述里取出对应语义位, 再按 type 指定的来源重写取出的文案</p>
 * <p>原始文案来源: IEnum 取 getValue(index), 普通枚举取 name(), 其他取 toString()</p>
 * <p>实际序列化器由 ddd-action 的 JsonTranslateSerializerModifier 注入</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@JacksonAnnotationsInside
public @interface JsonTranslate {

    /**
     * 语义位下标
     *
     * <p>枚举描述带多套语义时指定取第几套, 越界或无多套语义回落首位</p>
     *
     * @return 语义位下标, 从 0 开始
     */
    int index() default 0;

    /**
     * 翻译方式
     *
     * <p>决定语义位文案取出后按哪种来源重写</p>
     *
     * @return 翻译方式
     */
    CommonEnum.TranslateType type() default CommonEnum.TranslateType.FIX;

}
