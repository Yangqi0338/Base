package com.newzkl.platform.base.common.core.model.enums;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.util.Arrays;
import java.util.Map;

/**
 * 通用枚举反序列化器：按 name 或字段值 (含 code) 匹配枚举常量
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.web.config.WebJsonConverterConfig.EnumJsonDeserializer},
 * 改为持有精确枚举类型 (由 {@link EnumDeserializerModifier} 注入), 不再依赖运行时反射反查字段。</p>
 *
 * <p>无匹配 (含越界数字、空白、未知 code) 一律返回 {@code null}, 不抛异常,
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
            if (StrUtil.isBlank(text) || enumClass == null) {
                return null;
            }
            return matchEnum(enumClass, text);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * 按 name 或字段值匹配枚举常量
     *
     * <p>优先 name 精确匹配; 枚举未标 {@link JsonCreator} 时回退比对全部字段值
     * (含 code), 使前端传入的 code 字符串/数字可命中</p>
     *
     * @param clazz 枚举类型
     * @param text  待匹配文本
     * @return 匹配的枚举常量, 无匹配返回 {@code null}
     */
    private static Enum<?> matchEnum(Class<? extends Enum<?>> clazz, String text) {
        boolean hasJsonCreator = Arrays.stream(clazz.getDeclaredMethods())
                .anyMatch(it -> it.isAnnotationPresent(JsonCreator.class));
        Enum<?>[] constants = clazz.getEnumConstants();
        if (constants == null) {
            return null;
        }
        for (Enum<?> constant : constants) {
            if (constant.name().equals(text)) {
                return constant;
            }
            if (!hasJsonCreator) {
                Map<String, Object> map = BeanUtil.beanToMap(constant);
                Map<String, String> strMap = MapUtil.map(map, (k, v) -> v == null ? null : v.toString());
                if (strMap.containsValue(text)) {
                    return constant;
                }
            }
        }
        return null;
    }
}
