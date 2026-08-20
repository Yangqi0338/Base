package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description: 供应商角色策略
 * @date 2024/1/911:42
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SupplierIdentityPolicy extends AbsIdentityPolicy {

    private final SupplierClientDomain supplierDomain;

    @Override
    public AccountEnum.Identity support() {
        return AccountEnum.Identity.SUPPLIER;
    }

    @Override
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        //注册角色
        Long accountId = customSaveReq.getId();
        SupplierCustomSaveReq supplierUser = new SupplierCustomSaveReq();
        supplierUser.setId(accountId);
        supplierUser.setUsername(customSaveReq.getUsername());
        supplierUser.setInviteId(customSaveReq.getInviteId());
        supplierDomain.supplierCustomSave(supplierUser);

        return null;
    }

    @Override
    public Object detail(Long id) {
        return null;
    }

    @Override
    public boolean destroy(AccountVO accountVO, String destroyReason) {
        return false;
    }

    @Override
    public void saveByAccount(AccountReq req) {

    }
}
