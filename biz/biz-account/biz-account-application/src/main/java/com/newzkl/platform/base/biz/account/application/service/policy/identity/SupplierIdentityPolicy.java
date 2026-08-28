package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

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
    private final SupplierRepository supplierRepository;

    @Override
    public AccountEnum.Identity support() {
        return AccountEnum.Identity.SUPPLIER;
    }

    @Override
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        //注册角色
        Long accountId = customSaveReq.getId();
        SupplierVO item = new SupplierVO();
        item.setId(accountId);
        item.setState(SupplierEnum.State.INIT);
        item.setAuditState(AuditEnum.State.CUSTOM);
        item.setPromisePayState(CommonEnum.YesOrNo.NO);
        item.setPromisePayAuditState(AuditEnum.State.CUSTOM);
        item.setPeriodSetState(CommonEnum.YesOrNo.NO);
        // 前期固定5000
        item.setShouldPromisePayAmount(Money.of("5000"));
        item.setPromisePayConfig(0);
        supplierRepository.supplierSave(item);

        return new IdentityRegisterRes(null, accountId);
    }

    @Override
    public Object detail(Long id) {
        return null;
    }

    @Override
    public boolean destroy(AccountVO accountVO, String destroyReason) {
        // 移除供应商身份: 删 supplier 行
        supplierRepository.supplierDelete(Collections.singletonList(accountVO.getId()));
        return true;
    }

    @Override
    public void saveByAccount(AccountReq req) {

    }
}
