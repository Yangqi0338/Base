package com.newzkl.platform.base.biz.auth.action.aspect;

import cn.hutool.core.util.ArrayUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * 角色访问限制切面
 *
 * <p>拦截标注 {@link RoleLimit} 的类或方法, 取当前登录者角色, 不在注解声明的允许集合内则抛
 * {@link PlatformException}({@code NO_AUTH})。注解 {@code value} 为空视为不限制, 直接放行。</p>
 *
 * @author fang
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class RoleLimitAspect {

    /**
     * 环绕通知: 校验当前登录者角色是否在允许集合内
     *
     * @param pjp       连接点
     * @return 目标方法返回值
     * @throws Throwable 目标方法抛出的异常
     */
    @Around("@within(com.newzkl.platform.base.common.ddd.action.auth.RoleLimit) || " +
            "@annotation(com.newzkl.platform.base.common.ddd.action.auth.RoleLimit)")
    public Object check(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        Class<?> targetClass = ClassUtils.getUserClass(pjp.getTarget().getClass());
        RoleLimit roleLimit = method.getAnnotation(RoleLimit.class);
        if (roleLimit == null) {
            roleLimit = targetClass.getAnnotation(RoleLimit.class);
        }
        if (roleLimit == null) {
            return pjp.proceed();
        }
        AccountEnum.Identity[] allowed = roleLimit.value();
        AccountEnum.Client[] clientAllowed = roleLimit.client();
        if (ArrayUtil.isEmpty(allowed) && ArrayUtil.isEmpty(clientAllowed)) {
            return pjp.proceed();
        }
        AccountEnum.Identity current = SecurityUtils.getIdentity();
        if (current != null) {
            if (ArrayUtil.contains(allowed, current)) {
                return pjp.proceed();
            }
            if (ArrayUtil.contains(clientAllowed, current.getClient())) {
                return pjp.proceed();
            }
        }
        throw new PlatformException(BaseErrorCode.NO_AUTH);
    }
}
