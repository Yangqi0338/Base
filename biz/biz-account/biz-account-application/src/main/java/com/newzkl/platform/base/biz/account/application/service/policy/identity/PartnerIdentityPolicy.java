package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description: 服务商角色策略
 * @date 2024/1/911:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PartnerIdentityPolicy extends AbsIdentityPolicy {

    @Override
    public AccountEnum.Identity support() {
        return AccountEnum.Identity.PARTNER;
    }

    @Override
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        // 服务商角色无额外初始化, 直接返回成功
        return new IdentityRegisterRes(null, customSaveReq.getId());
    }

    @Override
    public Object detail(Long id) {
        return null;
    }

    @Override
    public boolean destroy(AccountVO accountVO, String destroyReason) {
        // 服务商无独立身份实体, 无需清理
        return true;
    }

    @Override
    public void saveByAccount(AccountReq req) {

    }
}
