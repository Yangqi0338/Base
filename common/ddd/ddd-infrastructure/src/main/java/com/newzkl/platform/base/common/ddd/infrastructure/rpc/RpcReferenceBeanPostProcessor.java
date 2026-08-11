package com.newzkl.platform.base.common.ddd.infrastructure.rpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * {@link RpcReference} 字段注入处理器
 *
 * <p>单体部署: 扫描每个 bean 的 {@code @RpcReference} 字段, 按字段类型从上下文取本地 provider bean 反射注入。
 * 取不到时按 {@link RpcReference#required()} 决定抛异常或告警跳过。将来拆微服务在此切换为 Dubbo consumer 生成逻辑,
 * 业务类注解与字段声明均不变</p>
 *
 * @author KC
 */
@Slf4j
@Component
public class RpcReferenceBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化前注入 {@code @RpcReference} 字段
     *
     * @param bean     待处理 bean
     * @param beanName bean 名称
     * @return 原 bean
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        ReflectionUtils.doWithFields(clazz, field -> injectField(bean, field));
        return bean;
    }

    /**
     * 注入单个字段
     *
     * @param bean  目标 bean
     * @param field 字段
     */
    private void injectField(Object bean, Field field) {
        RpcReference annotation = field.getAnnotation(RpcReference.class);
        if (annotation == null) {
            return;
        }
        Class<?> type = field.getType();
        Object provider = applicationContext.getBeanProvider(type).getIfAvailable();
        if (provider == null) {
            if (annotation.required()) {
                throw new NoSuchBeanDefinitionException(type,
                        "@RpcReference 要求的本地 provider 不存在: " + type.getName());
            }
            log.warn("@RpcReference 未找到本地 provider, 该引用留空: {}#{} ({})",
                    bean.getClass().getName(), field.getName(), type.getName());
            return;
        }
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, provider);
    }
}
