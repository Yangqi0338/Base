package com.newzkl.platform.base.common.ddd.action.auth;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 功能权限鉴权切面。
 *
 * <p>拦截标注 {@link FuncPermission} 的类或方法, 从 sa-token 会话取当前登录者权限清单,
 * 权限含通配 {@code *} 或对应功能码则放行, 否则抛 {@link NotPermissionException}。</p>
 *
 * @author fang
 */
@Slf4j
@Aspect
@Component
@Order(0)
public class FuncPermissionAspect {

    /**
     * 环绕通知: 校验当前登录者是否具备目标功能码。
     *
     * @param pjp 连接点
     * @return 目标方法返回值
     * @throws Throwable 目标方法抛出的异常
     */
    @Around("@within(com.newzkl.platform.base.common.ddd.action.auth.FuncPermission) "
            + "|| @annotation(com.newzkl.platform.base.common.ddd.action.auth.FuncPermission)")
    public Object check(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        Class<?> targetClass = ClassUtils.getUserClass(pjp.getTarget().getClass());

        String code = resolveCode(method, targetClass);
        if (code == null) {
            return pjp.proceed();
        }

        List<String> perms = StpUtil.getPermissionList();
        if (perms == null || (!perms.contains("*") && !perms.contains(code))) {
            throw new NotPermissionException(code, StpUtil.getLoginType());
        }
        return pjp.proceed();
    }

    /**
     * 解析目标功能码, 方法级注解优先于类级。
     *
     * @param method      目标方法
     * @param targetClass 目标类
     * @return 功能码, 无注解时返回 null
     */
    private String resolveCode(Method method, Class<?> targetClass) {
        FuncPermission m = method.getAnnotation(FuncPermission.class);
        if (m != null) {
            return m.code().isEmpty() ? compactClassName(targetClass) + "#" + method.getName() : m.code();
        }
        FuncPermission c = targetClass.getAnnotation(FuncPermission.class);
        if (c != null) {
            return c.code().isEmpty() ? compactClassName(targetClass) : c.code();
        }
        return null;
    }

    /**
     * 压缩类全名为 {@code {domain}.XxxController} 形式。
     *
     * <p>剥离 Base 包前缀 {@code com.newzkl.platform.base.biz.} 与中段 {@code .action.controller}。</p>
     *
     * @param cls 目标类
     * @return 压缩后的类名
     */
    private String compactClassName(Class<?> cls) {
        return cls.getName()
                .replaceFirst("^com\\.newzkl\\.platform\\.base\\.biz\\.", "")
                .replaceFirst("\\.action\\.controller\\.", ".");
    }
}
