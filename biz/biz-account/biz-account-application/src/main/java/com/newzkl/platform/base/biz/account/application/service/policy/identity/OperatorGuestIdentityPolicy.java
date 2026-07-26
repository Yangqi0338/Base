package com.newzkl.platform.base.biz.account.application.service.policy.identity;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description: 空账号角色策略
 * @date 2024/1/911:42
 */
@Component
@RequiredArgsConstructor
public class OperatorGuestIdentityPolicy extends AbsIdentityPolicy {

    private final UserQueryService accountQueryAppService;

    @Override
    public RoleEnum.CompanyRole support() {
        return RoleEnum.CompanyRole.OPERATOR_GUEST;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(identityAssembler.identityCustomReq2AccountCustomReq(customSaveReq));
        Long accountId = account.getId();
//        if (account.isOld()) {
//            if (!RoleEnum.State.DESTROY.getCode().equals(account.getState())) {
//                return new IdentityRegisterRes(AccountErrorCode.EXIST_ROLE, accountId);
//            }
//        }

        //获取邀请人信息
        AccountVO inviteAccountVO = accountQueryAppService.accountByYqm(customSaveReq.getYqm());

        //账号添加角色 和 层级关系
        accountService.roleAddEvent(account, inviteAccountVO, support(), customSaveReq.getPassword());

        if (inviteAccountVO != null) {
            RoleEnum.CompanyRole inviteAccountRole = CollUtil.getLast(RoleEnumUtil.getOperatorLevelUpEnumList(
                    inviteAccountVO.getRoleIdList()));
            AccountVO sourceAccount = new AccountVO();
            sourceAccount.setId(accountId);
            AbsIdentityPolicySupport.getPolicy(inviteAccountRole.getCode()).inviteSuccess(inviteAccountVO, sourceAccount, account);
        }
        return new IdentityRegisterRes(null, accountId);
    }

    /**
     * 运营商游客不支持代理注册。
     *
     * <p>迁移说明: 旧实现为 {@code void proxyRegister(String)} 空方法体(静默无操作)。
     * 新签名要求返回 {@link IdentityRegisterRes}, 无法保留"空实现"语义,
     * 故显式抛出不支持异常, 避免调用方拿到 null 后 NPE。</p>
     *
     * @param proxySaveReq 代理注册参数
     * @return 不会正常返回
     * @author KC
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {
        throw new ScmException(AccountErrorCode.NO_INVITE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleApplyAuditEvent(AuditEvent auditEvent) {
    }

    @Override
    public void inviteSuccess(AccountVO account, AccountVO inviteAccount, Object roleObj) {
        // 游客无法邀请人
        throw new ScmException(AccountErrorCode.NO_INVITE);
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
