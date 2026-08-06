package com.newzkl.platform.base.biz.account.domain.policy;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账号策略工厂
 *
 * @author fang
 */
@Slf4j
public class AbsAccountPolicySupport {

    private static final Map<CommonEnum.Client, AbsAccountPolicy> POLICY_MAP = new HashMap<>();

    /**
     * 根据端获取对应的处理器
     *
     * @param client 端
     */
    public static AbsAccountPolicy getPolicy(CommonEnum.Client client) {
        return POLICY_MAP.get(client);
    }

    @Autowired
    public void setAccountPolicyList(List<AbsAccountPolicy> accountPolicyList) {
        accountPolicyList.forEach(x -> {
            POLICY_MAP.put(x.support(), x);
        });
    }
}