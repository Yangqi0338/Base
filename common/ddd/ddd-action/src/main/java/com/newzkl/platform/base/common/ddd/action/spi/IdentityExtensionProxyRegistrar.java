package com.newzkl.platform.base.common.ddd.action.spi;

import com.newzkl.platform.base.common.ddd.application.spi.IdentityExtension;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * 身份扩展点透明代理注册器
 *
 * <p>启动早期扫描类路径上标注 {@code IdentityExtension} 的接口, 为每个接口注册一个 {@code @Primary} 的
 * {@code IdentityExtensionProxyFactoryBean} bean。调用方按接口类型注入时命中该主代理 bean (而非某个具体实现 bean),
 * 从而透明地获得 {@code IdentityDispatcher} 的分发代理。</p>
 *
 * @author KC
 */
@Component
public class IdentityExtensionProxyRegistrar implements BeanDefinitionRegistryPostProcessor {

    /** 扫描根包, 覆盖平台全部 biz 与 plugin 扩展点。 */
    private static final String BASE_PACKAGE = "com.newzkl.platform";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner = buildScanner();
        for (BeanDefinition candidate : scanner.findCandidateComponents(BASE_PACKAGE)) {
            String className = candidate.getBeanClassName();
            if (className == null) {
                continue;
            }
            Class<?> extensionType = resolveClass(className);
            if (extensionType == null || !extensionType.isInterface()) {
                continue;
            }
            registerProxyBean(registry, extensionType);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 无需后处理 bean factory。
    }

    /**
     * 构建仅接受 {@code IdentityExtension} 标注接口的扫描器
     *
     * <p>使用扫描器自带的默认 {@code StandardEnvironment}, 避免依赖 {@code EnvironmentAware}
     * (BDRPP 阶段 aware 回调不可靠)。</p>
     *
     * @return 组件扫描器
     */
    private ClassPathScanningCandidateComponentProvider buildScanner() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false) {
                    @Override
                    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                        // 扩展点是被标注的独立接口, 覆盖默认 (默认排除接口)。
                        return beanDefinition.getMetadata().isInterface()
                                && beanDefinition.getMetadata().isIndependent();
                    }
                };
        scanner.addIncludeFilter(new AnnotationTypeFilter(IdentityExtension.class));
        return scanner;
    }

    /**
     * 为扩展点接口注册 {@code @Primary} 代理 bean
     *
     * @param registry      bean 定义注册表
     * @param extensionType 扩展点接口
     */
    private void registerProxyBean(BeanDefinitionRegistry registry, Class<?> extensionType) {
        String beanName = extensionType.getName() + "#identityProxy";
        if (registry.containsBeanDefinition(beanName)) {
            return;
        }
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(IdentityExtensionProxyFactoryBean.class);
        definition.setPrimary(true);
        definition.setAutowireCandidate(true);
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, extensionType);
        definition.setConstructorArgumentValues(args);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
        registry.registerBeanDefinition(beanName, definition);
    }

    /**
     * 按类名加载 Class, 加载失败返回 {@code null}
     *
     * @param className 全限定类名
     * @return Class 或 null
     */
    private Class<?> resolveClass(String className) {
        try {
            return ClassUtils.forName(className, getClass().getClassLoader());
        } catch (ClassNotFoundException | LinkageError e) {
            return null;
        }
    }
}
