package com.newzkl.platform.base.biz.auth.action.config;


import com.newzkl.platform.base.common.ddd.action.auth.SecurityContextFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * servlet 安全上下文过滤器配置
 *
 * <p>注册 {@link SecurityContextFilter} 填充请求透传上下文。鉴权与 CORS 由
 * {@link SaTokenConfigure} 的 SaServletFilter 统一处理</p>
 *
 * @author KC
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    /**
     * 安全上下文过滤器注册
     *
     * @return 过滤器注册 bean
     */
    @Bean
    public FilterRegistrationBean<SecurityContextFilter> indexFilterRegistration() {
        FilterRegistrationBean<SecurityContextFilter> registration =
                new FilterRegistrationBean<>(new SecurityContextFilter());
        registration.addUrlPatterns("/*");
        // 早于 SaServletFilter(sa-token 默认 order = -100)执行, 保证鉴权前上下文已填充
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
