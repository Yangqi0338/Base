package com.newzkl.platform.base.common.ddd.action.spi;

import com.newzkl.platform.base.common.ddd.application.spi.IdentityExtension;
import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 身份扩展点注册器。
 *
 * <p>启动期 (所有单例实例化完成后) 扫描全部 {@link IdentityImpl} bean, 按其实现的
 * {@link IdentityExtension} 扩展点接口分组, 对同一扩展点下各实现的身份条件执行两两不相交校验,
 * 校验通过则填充 {@link IdentityDispatcher} 注册表, 违反则抛 {@link IllegalStateException} 阻止启动。</p>
 *
 * @author KC
 */
@Component
public class IdentityExtensionRegistrar implements SmartInitializingSingleton, ApplicationContextAware {

    private final IdentityDispatcher dispatcher;

    private ApplicationContext applicationContext;

    /**
     * @param dispatcher 分发器
     */
    public IdentityExtensionRegistrar(IdentityDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> impls = applicationContext.getBeansWithAnnotation(IdentityImpl.class);
        // 扩展点接口 → 该扩展点下的实现描述列表
        Map<Class<?>, List<ImplDescriptor>> grouped = new LinkedHashMap<>();
        for (Object bean : impls.values()) {
            Class<?> beanClass = ClassUtils.getUserClass(bean);
            IdentityImpl anno = AnnotationUtils.findAnnotation(beanClass, IdentityImpl.class);
            if (anno == null) {
                continue;
            }
            long[] condition = anno.value();
            for (Class<?> ext : resolveExtensionInterfaces(beanClass)) {
                grouped.computeIfAbsent(ext, k -> new ArrayList<>())
                        .add(new ImplDescriptor(bean, beanClass, condition));
            }
        }
        grouped.forEach(this::registerExtensionPoint);
    }

    /**
     * 解析实现类所实现的、标注 {@link IdentityExtension} 的接口集合。
     *
     * @param beanClass 实现类
     * @return 扩展点接口列表
     */
    private List<Class<?>> resolveExtensionInterfaces(Class<?> beanClass) {
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> itf : ClassUtils.getAllInterfacesForClass(beanClass)) {
            if (AnnotationUtils.findAnnotation(itf, IdentityExtension.class) != null) {
                result.add(itf);
            }
        }
        return result;
    }

    /**
     * 校验并登记单个扩展点的全部实现。
     *
     * @param ext         扩展点接口
     * @param descriptors 实现描述列表
     * @throws IllegalStateException 条件重叠 (含多个 catch-all) 时抛出
     */
    private void registerExtensionPoint(Class<?> ext, List<ImplDescriptor> descriptors) {
        checkPairwiseDisjoint(ext, descriptors);
        for (ImplDescriptor d : descriptors) {
            dispatcher.register(ext, d.bean(), d.condition());
        }
    }

    /**
     * 对同一扩展点下所有实现的身份条件执行两两不相交校验。
     *
     * <p>任意两实现条件集交集非空即冲突; 两个 catch-all (空条件) 视为重叠冲突。</p>
     *
     * @param ext         扩展点接口
     * @param descriptors 实现描述列表
     * @throws IllegalStateException 存在重叠时抛出, 消息含冲突两方类名与重叠 code
     */
    private void checkPairwiseDisjoint(Class<?> ext, List<ImplDescriptor> descriptors) {
        for (int i = 0; i < descriptors.size(); i++) {
            for (int j = i + 1; j < descriptors.size(); j++) {
                ImplDescriptor a = descriptors.get(i);
                ImplDescriptor b = descriptors.get(j);
                boolean aCatchAll = a.condition() == null || a.condition().length == 0;
                boolean bCatchAll = b.condition() == null || b.condition().length == 0;
                if (aCatchAll && bCatchAll) {
                    throw new IllegalStateException(String.format(
                            "扩展点 %s 存在多个 catch-all 兜底实现: %s 与 %s",
                            ext.getName(), a.implClass().getName(), b.implClass().getName()));
                }
                List<Long> overlap = intersection(a.condition(), b.condition());
                if (!overlap.isEmpty()) {
                    throw new IllegalStateException(String.format(
                            "扩展点 %s 身份条件重叠: %s%s ∩ %s%s = %s",
                            ext.getName(),
                            a.implClass().getName(), Arrays.toString(a.condition()),
                            b.implClass().getName(), Arrays.toString(b.condition()),
                            overlap));
                }
            }
        }
    }

    /**
     * 求两身份条件集的交集。
     *
     * @param x 条件集 x
     * @param y 条件集 y
     * @return 交集 code 列表, 无交集为空列表
     */
    private List<Long> intersection(long[] x, long[] y) {
        List<Long> overlap = new ArrayList<>();
        if (x == null || y == null) {
            return overlap;
        }
        for (long a : x) {
            for (long b : y) {
                if (a == b && !overlap.contains(a)) {
                    overlap.add(a);
                }
            }
        }
        return overlap;
    }

    /**
     * 实现描述。
     *
     * @param bean      实现实例
     * @param implClass 实现类 (已解包 AOP 代理)
     * @param condition 身份条件集
     */
    private record ImplDescriptor(Object bean, Class<?> implClass, long[] condition) {
    }
}
