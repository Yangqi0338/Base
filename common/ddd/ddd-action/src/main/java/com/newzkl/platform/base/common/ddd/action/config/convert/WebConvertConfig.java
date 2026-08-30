package com.newzkl.platform.base.common.ddd.action.config.convert;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * query/form 参数转换装配
 *
 * <p>以 {@link WebMvcConfigurer} 注册 (Boot 的 {@code WebMvcAutoConfiguration} 会汇总全部
 * configurer 后调 {@code addFormatters}), 不用 {@code @EnableWebMvc}: 后者会关掉
 * Boot 的 WebMvc 自动配置</p>
 *
 * @author KC
 */
@Configuration
public class WebConvertConfig implements WebMvcConfigurer {

    /**
     * {@inheritDoc}
     *
     * <p>注册枚举转换工厂, 使 query/form 参数可传 code</p>
     */
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverterFactory());
    }
}
