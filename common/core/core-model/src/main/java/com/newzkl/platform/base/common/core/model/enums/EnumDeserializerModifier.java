package com.newzkl.platform.base.common.core.model.enums;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

/**
 * 枚举反序列化修饰器：为所有枚举装配 {@link EnumValueJsonDeserializer}
 *
 * <p>迁移自 new-scm/adopt-chicken WebJsonConverterConfig 的全局枚举反序列化能力,
 * 使前端传入的 code (字符串或数字) 按枚举字段值匹配, 越界或未知值返回 {@code null}。
 * 集合元素枚举由 Jackson 默认集合装配自动复用此元素反序列化器, 无需单独处理</p>
 *
 * @author KC
 */
public class EnumDeserializerModifier extends BeanDeserializerModifier {

    /**
     * {@inheritDoc}
     *
     * <p>枚举类型替换为按字段值匹配的反序列化器</p>
     */
    @Override
    @SuppressWarnings("unchecked")
    public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config,
                                                      JavaType type,
                                                      BeanDescription beanDesc,
                                                      JsonDeserializer<?> defaultDeserializer) {
        if (type.isEnumType()) {
            return new EnumValueJsonDeserializer((Class<? extends Enum<?>>) type.getRawClass());
        }
        return defaultDeserializer;
    }
}
