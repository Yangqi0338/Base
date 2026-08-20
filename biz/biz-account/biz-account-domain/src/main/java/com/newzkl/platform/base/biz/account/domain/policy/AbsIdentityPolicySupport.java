package com.newzkl.platform.base.biz.account.domain.policy;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 */
@Component
@Slf4j
public class AbsIdentityPolicySupport implements InitializingBean, ApplicationContextAware {

    private static final Map<AccountEnum.Identity, AbsIdentityPolicy> POLICY_MAP = new HashMap<>();

    private ApplicationContext appContext;

    /**
     * 根据类型获取对应的处理器
     *
     * @param roleId 类型
     * @return 类型对应的处理器
     */
    public static AbsIdentityPolicy getPolicy(Long roleId) {
        return POLICY_MAP.get(AccountEnum.Identity.getByCode(roleId));
    }

    public static AbsIdentityPolicy getPolicy(AccountEnum.Identity identity) {
        return POLICY_MAP.get(identity);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 将 Spring 容器中所有的 LoginHandler 注册到 POLICY_MAP
        appContext.getBeansOfType(AbsIdentityPolicy.class)
                .values()
                .forEach(handler -> POLICY_MAP.put(handler.support(), handler));
        log.info("afterPropertiesSet");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}