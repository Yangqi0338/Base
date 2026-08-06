package com.newzkl.platform.base.common.ddd.action.auth;

import cn.hutool.core.util.ArrayUtil;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
     * @param roleLimit 命中的角色限制注解
     * @return 目标方法返回值
     * @throws Throwable 目标方法抛出的异常
     */
    @Around("@within(roleLimit) || @annotation(roleLimit)")
    public Object check(ProceedingJoinPoint pjp, RoleLimit roleLimit) throws Throwable {
        RoleEnum.CompanyRole[] allowed = roleLimit.value();
        CommonEnum.Client[] clientAllowed = roleLimit.client();
        if (ArrayUtil.isEmpty(allowed) && ArrayUtil.isEmpty(clientAllowed)) {
            return pjp.proceed();
        }
        RoleEnum.CompanyRole current = SecurityUtils.getRole();
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
