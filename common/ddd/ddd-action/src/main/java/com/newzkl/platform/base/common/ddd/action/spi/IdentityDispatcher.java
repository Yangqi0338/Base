package com.newzkl.platform.base.common.ddd.action.spi;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 身份扩展点分发器
 *
 * <p>维护 扩展点接口 → 实现列表 的注册表, 对外产出 JDK 动态代理。代理在方法调用时读取当前调用方身份
 * 选出唯一命中实现执行一次; 无精确命中则回退 catch-all 实现;
 * 仍无命中则抛出 {@code PlatformException}。注册表由 {@code IdentityExtensionRegistrar} 启动期填充。</p>
 *
 * @author KC
 */
@Component
public class IdentityDispatcher {

    /** 扩展点接口 → 实现持有者列表。 */
    private final Map<Class<?>, List<ImplHolder>> registry = new ConcurrentHashMap<>();

    /** 扩展点接口 → 已产出的代理实例缓存。 */
    private final Map<Class<?>, Object> proxyCache = new ConcurrentHashMap<>();

    /**
     * 注册某扩展点的一个实现
     *
     * @param ext       扩展点接口
     * @param impl      实现实例
     * @param condition 命中身份 code 集, 空为 catch-all
     */
    public void register(Class<?> ext, Object impl, RoleEnum.CompanyRole[] condition) {
        registry.computeIfAbsent(ext, k -> new ArrayList<>())
                .add(new ImplHolder(impl, condition));
    }

    /**
     * 判断某扩展点是否已登记实现
     *
     * @param ext 扩展点接口
     * @return 已登记返回 {@code true}
     */
    public boolean hasRegistered(Class<?> ext) {
        return registry.containsKey(ext);
    }

    /**
     * 产出扩展点的分发代理
     *
     * <p>返回的代理按调用方身份路由到唯一实现。同一扩展点多次调用返回同一缓存代理。</p>
     *
     * @param ext 扩展点接口
     * @param <T> 扩展点类型
     * @return 分发代理实例
     */
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> ext) {
        return (T) proxyCache.computeIfAbsent(ext, this::createProxy);
    }

    /**
     * 为扩展点创建 JDK 动态代理
     *
     * @param ext 扩展点接口
     * @return 代理实例
     */
    private Object createProxy(Class<?> ext) {
        return Proxy.newProxyInstance(
                ext.getClassLoader(),
                new Class<?>[]{ext},
                new DispatchHandler(ext));
    }

    /**
     * 按身份选出命中实现
     *
     * @param ext    扩展点接口
     * @param role 调用方身份 code, 可能为 null
     * @return 命中实现实例
     * @throws PlatformException 无任何命中且无 catch-all 时抛出
     */
    private Object selectImpl(Class<?> ext, RoleEnum.CompanyRole role) {
        List<ImplHolder> holders = registry.get(ext);
        ImplHolder catchAll = null;
        if (holders != null) {
            for (ImplHolder holder : holders) {
                if (holder.catchAll()) {
                    catchAll = holder;
                    continue;
                }
                if (holder.matches(role)) {
                    return holder.impl();
                }
            }
            if (catchAll != null) {
                return catchAll.impl();
            }
        }
        throw new PlatformException(BaseErrorCode.NO_AUTH.getCode(),
                "该请求不支持当前身份[" + role + "]");
    }

    /**
     * 分发调用处理器
     *
     * <p>拦截扩展点代理的方法调用, 按身份选实现后转发。</p>
     */
    private final class DispatchHandler implements InvocationHandler {

        private final Class<?> ext;

        private DispatchHandler(Class<?> ext) {
            this.ext = ext;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return handleObjectMethod(proxy, method, args);
            }
            RoleEnum.CompanyRole role = SecurityUtils.getRole();
            Object impl = selectImpl(ext, role);
            try {
                return method.invoke(impl, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

        /**
         * 处理 {@code Object} 声明的方法 (toString/hashCode/equals)
         *
         * @param proxy  代理实例
         * @param method 方法
         * @param args   参数
         * @return 结果
         */
        private Object handleObjectMethod(Object proxy, Method method, Object[] args) {
            switch (method.getName()) {
                case "toString":
                    return "IdentityDispatchProxy[" + ext.getName() + "]";
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "equals":
                    return proxy == (args == null ? null : args[0]);
                default:
                    return null;
            }
        }
    }

    /**
     * 实现持有者
     *
     * @param impl      实现实例
     * @param condition 命中身份 code 集, 空为 catch-all
     */
    public record ImplHolder(Object impl, RoleEnum.CompanyRole[] condition) {

        /**
         * 是否为 catch-all 兜底实现
         *
         * @return 条件为空返回 {@code true}
         */
        public boolean catchAll() {
            return condition == null || condition.length == 0;
        }

        /**
         * 条件是否命中给定身份
         *
         * @param role 身份 code
         * @return 命中返回 {@code true}
         */
        public boolean matches(RoleEnum.CompanyRole role) {
            for (RoleEnum.CompanyRole c : condition) {
                if (c == role) {
                    return true;
                }
            }
            return false;
        }
    }
}
