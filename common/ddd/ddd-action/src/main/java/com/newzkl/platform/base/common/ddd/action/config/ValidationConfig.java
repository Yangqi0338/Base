package com.newzkl.platform.base.common.ddd.action.config;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

/**
 * 校验国际化配置
 *
 * <p>将 Bean Validation 的消息插值语言钉死为简体中文, 使校验默认文案统一走 hibernate-validator
 * 自带的 {@code ValidationMessages_zh_CN}(如 {@code @NotNull} → "不能为null"), 避免随请求
 * {@code Accept-Language} 头或 JVM 默认 Locale 漂移。</p>
 *
 * <p>字段业务名由全局异常处理器经 QDox 解析 javadoc 首行拼接, 与此处默认文案组合成
 * "[字段业务名]默认文案", 故各 Req 字段无需再手写 {@code message}。</p>
 *
 * @author KC
 */
@Configuration
public class ValidationConfig {

    /**
     * 语言锁定为简体中文的校验器工厂
     *
     * @return 使用固定 zh_CN 插值器的校验器工厂
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setMessageInterpolator(new FixedLocaleMessageInterpolator(Locale.CHINA));
        return factoryBean;
    }

    /**
     * 定长语言消息插值器
     *
     * <p>包装 {@link ResourceBundleMessageInterpolator}, 忽略入参 locale 与线程上下文,
     * 恒定使用构造时指定的 locale 进行插值。</p>
     */
    private static final class FixedLocaleMessageInterpolator implements MessageInterpolator {

        /**
         * 委托的资源包插值器
         */
        private final MessageInterpolator delegate = new ResourceBundleMessageInterpolator();
        /**
         * 固定插值语言
         */
        private final Locale locale;

        private FixedLocaleMessageInterpolator(Locale locale) {
            this.locale = locale;
        }

        @Override
        public String interpolate(String messageTemplate, Context context) {
            return delegate.interpolate(messageTemplate, context, locale);
        }

        @Override
        public String interpolate(String messageTemplate, Context context, Locale locale) {
            return delegate.interpolate(messageTemplate, context, this.locale);
        }
    }

    /**
     * 暴露 {@link Validator} 便于编程式校验注入
     *
     * @param factoryBean 校验器工厂
     * @return 校验器
     */
    @Bean
    public Validator beanValidator(LocalValidatorFactoryBean factoryBean) {
        return factoryBean;
    }
}
