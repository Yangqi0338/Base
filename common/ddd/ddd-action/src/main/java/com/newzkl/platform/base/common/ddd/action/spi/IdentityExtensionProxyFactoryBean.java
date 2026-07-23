package com.newzkl.platform.base.common.ddd.action.spi;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 身份扩展点代理工厂 bean。
 *
 * <p>为单个 {@link com.newzkl.platform.base.common.ddd.model.spi.IdentityExtension} 扩展点接口产出
 * {@link IdentityDispatcher} 的分发代理, 以 {@code @Primary} 身份登记, 使调用方按接口类型注入时拿到代理而非具体实现。
 * 代理内部按调用方身份路由到唯一实现执行。</p>
 *
 * @param <T> 扩展点类型
 * @author KC
 */
public class IdentityExtensionProxyFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private final Class<T> extensionType;

    private ApplicationContext applicationContext;

    /**
     * @param extensionType 扩展点接口类型
     */
    public IdentityExtensionProxyFactoryBean(Class<T> extensionType) {
        this.extensionType = extensionType;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public T getObject() {
        return applicationContext.getBean(IdentityDispatcher.class).resolve(extensionType);
    }

    @Override
    public Class<?> getObjectType() {
        return extensionType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
