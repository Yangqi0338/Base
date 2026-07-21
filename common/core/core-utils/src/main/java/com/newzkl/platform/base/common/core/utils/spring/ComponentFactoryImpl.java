package com.newzkl.platform.base.common.core.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


/**
 * {@link ComponentFactory} 的 Spring 实现。
 *
 * @author fang
 */
@Lazy(false) // 确保该 Bean 被 Spring 主动初始化（非懒加载）
@Service
public class ComponentFactoryImpl extends ComponentFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public <T> T _get(Class<T> clz) {
        try {
            return applicationContext.getBean(clz);
        } catch (BeansException e) {
            throw new IllegalArgumentException("未在 Spring 容器中找到类型为 " + clz.getName() + " 的 Bean", e);
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (instance == null) {
            instance = this;
        }
    }
}
