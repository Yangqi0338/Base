package com.newzkl.platform.base.common.ddd.action.config.convert;

import com.newzkl.platform.base.common.core.model.enums.EnumMatcher;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;

/**
 * query/form 参数枚举转换工厂
 *
 * <p>Spring 内置 {@code StringToEnumConverterFactory} 只按 {@code Enum.valueOf(name)} 匹配,
 * 前端在 {@code @RequestParam} / GET 对象参数上传 code (如 {@code ?type=1001}) 会抛
 * {@code ConversionFailedException} → 400。本工厂改按 {@link EnumMatcher} 匹配,
 * 与 JSON body 侧的枚举反序列化同口径, 未命中返回 {@code null} 交 validation 兜底</p>
 *
 * <p>必须由 {@link WebConvertConfig} 显式注册: {@code ConverterFactory} 类型的 bean 不被
 * {@code ApplicationConversionService.addBeans} 收集 (它只认
 * {@code GenericConverter}/{@code Converter}/{@code Printer}/{@code Parser}),
 * 仅打 {@code @Component} 是死代码</p>
 *
 * @author KC
 */
public class EnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public <T extends Enum<?>> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return new StringToEnum<>(targetType);
    }

    /**
     * 单个枚举类型的转换器
     *
     * @param <T> 目标枚举类型
     */
    private static class StringToEnum<T extends Enum<?>> implements Converter<String, T> {

        /**
         * 目标枚举类型
         */
        private final Class<T> enumType;

        /**
         * 构造器
         *
         * @param enumType 目标枚举类型
         */
        StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("unchecked")
        public T convert(@NonNull String source) {
            return (T) EnumMatcher.match(enumType, source);
        }
    }
}
