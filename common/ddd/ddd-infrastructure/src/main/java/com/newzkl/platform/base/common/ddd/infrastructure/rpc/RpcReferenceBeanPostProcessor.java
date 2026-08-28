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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * {@link RpcReference} 字段注入处理器
 *
 * <p>架构自适应三级降级链: dubbo 远程({@link DubboReferenceResolver} 策略) → 本地 autowire → null。
 * 扫描每个 bean 的 {@code @RpcReference} 字段, 按 {@link RpcReference#mode()} 决定是否先试远程, 远程无结果
 * 降级本地按类型取 provider bean, 仍取不到时按 {@link RpcReference#required()} 决定抛异常或告警留空。
 * 单体默认 {@link NoopDubboReferenceResolver}(available=false)使 AUTO 全走本地, 零回归; 拆微服务时以真实
 * resolver 覆盖即可, 业务类注解与字段声明均不变</p>
 *
 * @author KC
 */
@Slf4j
@Component
public class RpcReferenceBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private DubboReferenceResolver dubboResolver;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 惰性获取 dubbo 解析策略
     *
     * <p>避免 BPP 与 resolver bean 初始化顺序问题, 首次注入时才从上下文取, 无则退回 Noop</p>
     *
     * @return dubbo 解析策略
     */
    private DubboReferenceResolver resolver() {
        if (dubboResolver == null) {
            dubboResolver = applicationContext.getBeanProvider(DubboReferenceResolver.class)
                    .getIfAvailable(NoopDubboReferenceResolver::new);
        }
        return dubboResolver;
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
     * <p>注入惰性代理而非真实 provider: 代理在首次方法调用时才按降级链(dubbo→本地→报错)解析真实目标。
     * 借此打破单体部署下跨域 provider 相互构造注入形成的 Spring bean 循环 —— 注入阶段不触碰对端 bean,
     * 待全部 bean 就绪后首次调用再解析, 环自然断开。字段类型均为 facade 接口, JDK 动态代理可覆盖。</p>
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
        Object proxy = Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[]{type},
                new LazyRpcInvocationHandler(type, annotation));
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, proxy);
    }

    /**
     * 惰性解析目标 provider
     *
     * <p>降级链: dubbo 远程 → 本地按类型 autowire → null。仅在代理方法首次调用时触发, 结果缓存复用。</p>
     *
     * @param type       引用接口类型
     * @param annotation 引用注解
     * @return 真实目标, 无则 null
     */
    private Object resolveTarget(Class<?> type, RpcReference annotation) {
        Object target = null;
        // 1. dubbo 远程分支
        if (shouldTryDubbo(annotation.mode())) {
            target = resolver().resolve(type);
        }
        // 2. 本地 autowire 兜底(DUBBO/AUTO 远程无结果均降级本地, LOCAL 本就走此)
        if (target == null) {
            target = applicationContext.getBeanProvider(type).getIfAvailable();
        }
        return target;
    }

    /**
     * {@link RpcReference} 惰性代理调用处理器
     *
     * <p>首次业务方法调用时解析真实目标并缓存; 解析不到时按 {@link RpcReference#required()} 抛异常。
     * Object 自带方法(toString/hashCode/equals)不触发解析, 避免容器扫描期误解析破坏懒加载。</p>
     */
    private final class LazyRpcInvocationHandler implements InvocationHandler {

        private final Class<?> type;
        private final RpcReference annotation;
        private volatile Object target;

        LazyRpcInvocationHandler(Class<?> type, RpcReference annotation) {
            this.type = type;
            this.annotation = annotation;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            return method.invoke(resolveOnce(), args);
        }

        /**
         * 解析并缓存真实目标, 解析不到时报错
         *
         * @return 真实 provider
         */
        private Object resolveOnce() {
            Object resolved = target;
            if (resolved == null) {
                synchronized (this) {
                    resolved = target;
                    if (resolved == null) {
                        resolved = resolveTarget(type, annotation);
                        if (resolved == null) {
                            throw new NoSuchBeanDefinitionException(type,
                                    "@RpcReference 未找到可注入 provider: " + type.getName());
                        }
                        target = resolved;
                    }
                }
            }
            return resolved;
        }
    }

    /**
     * 是否尝试 dubbo 远程
     *
     * @param mode 注入模式
     * @return LOCAL 恒 false; DUBBO 恒 true; AUTO 看 resolver 可用性
     */
    private boolean shouldTryDubbo(RpcReference.Mode mode) {
        if (mode == RpcReference.Mode.LOCAL) {
            return false;
        }
        if (mode == RpcReference.Mode.DUBBO) {
            return true;
        }
        return resolver().available();
    }
}
