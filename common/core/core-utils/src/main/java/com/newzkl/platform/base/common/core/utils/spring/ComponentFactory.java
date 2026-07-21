package com.newzkl.platform.base.common.core.utils.spring;

/**
 * Spring 容器 Bean 访问工厂 (静态门面)。
 *
 * @author fang
 */
public abstract class ComponentFactory {

    // 添加 volatile 修饰，确保多线程下的可见性
    protected static volatile ComponentFactory instance;

    /**
     * 获取 Spring 容器中的 Bean 实例。
     *
     * @param clz 目标类型的 Class 对象
     * @param <T> Bean 类型
     * @return 容器中的 Bean 实例
     */
    public static <T> T get(Class<T> clz) {
        if (instance == null) {
            throw new IllegalStateException("ComponentFactory 未初始化完成，请确保 ComponentFactoryImpl 已被 Spring 正确加载");
        }
        return instance._get(clz);
    }

    protected abstract <T> T _get(Class<T> clz);
}
