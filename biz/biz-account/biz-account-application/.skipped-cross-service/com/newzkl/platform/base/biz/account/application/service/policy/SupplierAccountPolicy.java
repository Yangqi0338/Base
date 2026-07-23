package com.newzkl.platform.base.biz.account.application.service.policy;

import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;

import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicy;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.res.AccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountProxySaveReq;
import org.springframework.stereotype.Component;

/**
 * 供应商端账号策略
 *
 * @author muc_fang
 * @Description: 角色策略
 * @date 2024/1/911:42
 */
@Component
public class SupplierAccountPolicy extends AbsAccountPolicy {

    @Override
    public CommonEnum.Client support() {
        return CommonEnum.Client.SUPPLIER;
    }

    @Override
    public AccountRegisterRes customRegister(AccountCustomSaveReq customSaveReq) {
        return null;
    }

    @Override
    public AccountRegisterRes proxyRegister(AccountProxySaveReq proxySaveReq) {

    }

    @Override
    public void roleApplyAuditEvent(AuditEvent auditEvent) {

    }

    @Override
    public void inviteSuccess(AccountVO account, AccountRes inviteAccount, Object roleObj) {

    }

    @Override
    public Class<?> getEntityClass() {
        return null;
    }
}
