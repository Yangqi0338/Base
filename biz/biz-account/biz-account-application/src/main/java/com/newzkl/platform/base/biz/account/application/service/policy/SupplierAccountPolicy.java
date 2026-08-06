package com.newzkl.platform.base.biz.account.application.service.policy;

import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

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

    /**
     * 供应商端账号代理注册
     *
     * <p>迁移说明: 旧实现为 {@code void proxyRegister(String)} 空方法体(静默无操作)。
     * 新签名要求返回 {@code AccountRegisterRes}, 为保持与本类 {@link SupplierAccountPolicy#customRegister} 一致的
     * "未实现即返回 null" 语义, 此处返回 null。</p>
     *
     * @param proxySaveReq 代理注册参数
     * @return 恒为 null (未实现)
     * @author KC
     */
    @Override
    public AccountRegisterRes proxyRegister(AccountProxySaveReq proxySaveReq) {
        return null;
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
