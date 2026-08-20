package com.newzkl.platform.base.common.ddd.action.spi;

import com.newzkl.platform.base.common.ddd.application.spi.IdentityExtension;
import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code IdentityExtensionRegistrar} 启动期两两不相交校验单测
 *
 * @author KC
 */
class IdentityExtensionRegistrarTest {

    @Test
    void disjointConditionsRegisterSuccessfully() {
        IdentityDispatcher dispatcher = new IdentityDispatcher();
        IdentityExtensionRegistrar registrar = registrarWith(dispatcher, new PlatformFoo(), new ChannelFoo());

        registrar.afterSingletonsInstantiated();

        assertTrue(dispatcher.hasRegistered(Foo.class));
    }

    @Test
    void overlappingConditionsFailFast() {
        IdentityDispatcher dispatcher = new IdentityDispatcher();
        IdentityExtensionRegistrar registrar = registrarWith(dispatcher, new PlatformFoo(), new OverlapFoo());

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                registrar::afterSingletonsInstantiated);
        assertTrue(ex.getMessage().contains("身份条件重叠"), ex.getMessage());
        assertFalse(dispatcher.hasRegistered(Foo.class));
    }

    @Test
    void multipleCatchAllFailFast() {
        IdentityDispatcher dispatcher = new IdentityDispatcher();
        IdentityExtensionRegistrar registrar = registrarWith(dispatcher, new CatchAllFooA(), new CatchAllFooB());

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                registrar::afterSingletonsInstantiated);
        assertTrue(ex.getMessage().contains("catch-all"), ex.getMessage());
    }

    /**
     * 构造注入了指定 impl bean 的注册器 (以动态代理伪造 ApplicationContext)
     *
     * @param dispatcher 分发器
     * @param beans      impl 实例
     * @return 注册器
     */
    private IdentityExtensionRegistrar registrarWith(IdentityDispatcher dispatcher, Object... beans) {
        Map<String, Object> beanMap = new LinkedHashMap<>();
        for (int i = 0; i < beans.length; i++) {
            beanMap.put(beans[i].getClass().getSimpleName() + i, beans[i]);
        }
        ApplicationContext ctx = (ApplicationContext) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[]{ApplicationContext.class},
                (proxy, method, args) -> {
                    if ("getBeansWithAnnotation".equals(method.getName())) {
                        return beanMap;
                    }
                    return null;
                });
        IdentityExtensionRegistrar registrar = new IdentityExtensionRegistrar(dispatcher);
        registrar.setApplicationContext(ctx);
        return registrar;
    }

    /**
     * 测试扩展点
     */
    @IdentityExtension
    interface Foo {

        /**
         * @return 标识
         */
        String tag();
    }

    /** 平台条件实现 {1,2}。 */
    @IdentityImpl(identities = {AccountEnum.Identity.PLATFORM, AccountEnum.Identity.EMP})
    static class PlatformFoo implements Foo {
        @Override
        public String tag() {
            return "platform";
        }
    }

    /** 渠道条件实现 {1002} (与平台不相交)。 */
    @IdentityImpl(identities = AccountEnum.Identity.CHANNEL)
    static class ChannelFoo implements Foo {
        @Override
        public String tag() {
            return "channel";
        }
    }

    /** 与平台重叠实现 {2,3} (交集 {2})。 */
    @IdentityImpl(identities = {AccountEnum.Identity.PLATFORM, AccountEnum.Identity.EMP})
    static class OverlapFoo implements Foo {
        @Override
        public String tag() {
            return "overlap";
        }
    }

    /** catch-all A (空条件)。 */
    @IdentityImpl
    static class CatchAllFooA implements Foo {
        @Override
        public String tag() {
            return "a";
        }
    }

    /** catch-all B (空条件)。 */
    @IdentityImpl
    static class CatchAllFooB implements Foo {
        @Override
        public String tag() {
            return "b";
        }
    }
}
