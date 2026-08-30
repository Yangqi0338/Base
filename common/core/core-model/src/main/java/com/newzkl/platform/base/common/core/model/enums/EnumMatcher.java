package com.newzkl.platform.base.common.core.model.enums;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Map;

/**
 * 枚举文本匹配工具
 *
 * <p>入参枚举解析的唯一口径, 两条入口共用: JSON body 走
 * {@code EnumValueJsonDeserializer}, query/form 参数走
 * {@code EnumConverterFactory} (Spring ConversionService)。
 * 两处必须同口径, 否则同一枚举在 body 与 query 上行为分叉</p>
 *
 * @author KC
 */
public final class EnumMatcher {

    /**
     * 工具类禁止实例化
     */
    private EnumMatcher() {
    }

    /**
     * 按 name 或字段值匹配枚举常量
     *
     * <p>优先 name 精确匹配; 枚举未标 {@link JsonCreator} 时回退比对全部字段值
     * (含 code), 使前端传入的 code 字符串/数字可命中。
     * 无匹配 (含越界数字、空白、未知 code) 返回 {@code null}, 不抛异常, 合法性交 validation</p>
     *
     * @param clazz 枚举类型
     * @param text  待匹配文本
     * @return 匹配的枚举常量, 无匹配返回 {@code null}
     */
    public static Enum<?> match(Class<? extends Enum<?>> clazz, String text) {
        if (clazz == null || StrUtil.isBlank(text)) {
            return null;
        }
        Enum<?>[] constants = clazz.getEnumConstants();
        if (constants == null) {
            return null;
        }
        boolean hasJsonCreator = Arrays.stream(clazz.getDeclaredMethods())
                .anyMatch(it -> it.isAnnotationPresent(JsonCreator.class));
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
