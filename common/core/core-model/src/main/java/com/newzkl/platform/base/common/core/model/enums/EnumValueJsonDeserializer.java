package com.newzkl.platform.base.common.core.model.enums;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 通用枚举反序列化器：按 name 或字段值 (含 code) 匹配枚举常量
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.web.config.WebJsonConverterConfig.EnumJsonDeserializer},
 * 改为持有精确枚举类型 (由 {@link EnumDeserializerModifier} 注入), 不再依赖运行时反射反查字段。</p>
 *
 * <p>匹配逻辑下沉 {@link EnumMatcher}, 与 query/form 参数的枚举转换共用同一口径。
 * 无匹配 (含越界数字、空白、未知 code) 一律返回 {@code null}, 不抛异常,
 * 由 validation 层控制合法性</p>
 *
 * @author KC
 */
public class EnumValueJsonDeserializer extends JsonDeserializer<Enum<?>> {

    /**
     * 目标枚举类型
     */
    private final Class<? extends Enum<?>> enumClass;

    /**
     * 构造器
     *
     * @param enumClass 目标枚举类型
     */
    public EnumValueJsonDeserializer(Class<? extends Enum<?>> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) {
        try {
            String text = p.getText();
            if (StrUtil.isBlank(text)) {
                return null;
            }
            return EnumMatcher.match(enumClass, text);
        } catch (Exception ignored) {
            return null;
        }
    }
}
