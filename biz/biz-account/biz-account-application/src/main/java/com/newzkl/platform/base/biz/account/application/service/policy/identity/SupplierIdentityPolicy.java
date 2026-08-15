package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinancePurseApi;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.facade.InitFinanceReq;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author muc_fang
 * @Description: 供应商角色策略
 * @date 2024/1/911:42
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SupplierIdentityPolicy extends AbsIdentityPolicy {

    private final FinancePurseApi financePurseApi;
    private final UserQueryService roleQueryAppService;
    private final SupplierClientDomain supplierDomain;
    private final AccountDomain accountDomain;

    @Override
    public RoleEnum.CompanyRole support() {
        return RoleEnum.CompanyRole.SUPPLIER;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(identityAssembler.identityCustomReq2AccountCustomReq(customSaveReq));
        Long accountId = account.getId();
        //检查角色
        SupplierVO supplierVO = roleQueryAppService.supplierVO(accountId);
        if (supplierVO != null) {
            return new IdentityRegisterRes(AccountErrorCode.EXIST_ROLE, accountId);
        }
        //获取邀请人信息
        Long inviteAccountId = null;
        AccountVO inviteAccountVO = roleQueryAppService.accountByYqm(customSaveReq.getYqm());
        log.info("邀请人信息：{}", JSONUtil.toJsonStr(inviteAccountVO));
        if (inviteAccountVO != null) {
            if (accountId.equals(inviteAccountVO.getId())) {
                throw new PlatformException(AccountErrorCode.PARAM_YQM);
            } else {
                inviteAccountId = inviteAccountVO.getId();
            }
        }
        //注册角色
        SupplierCustomSaveReq supplierUser = new SupplierCustomSaveReq();
        supplierUser.setId(accountId);
        supplierUser.setUsername(customSaveReq.getUsername());
        supplierUser.setInviteId(inviteAccountId);
        supplierDomain.supplierCustomSave(supplierUser);
        //账号添加角色 (inviteAccount是在运营商体系下的,这里不能设置 TODO)
        accountDomain.roleAddEvent(account, RoleEnum.CompanyRole.SUPPLIER, customSaveReq.getPassword());
        //初始化财务
        InitFinanceReq initFinanceReq = new InitFinanceReq();
        initFinanceReq.setAccountId(accountId);
        initFinanceReq.setAccountName(customSaveReq.getUsername());
        initFinanceReq.setFinanceUser(PurseEnum.FinanceUser.SUPPLIER);
        initFinanceReq.setSubPurseType(null);
        if (inviteAccountId != null) {
            initFinanceReq.setParentId(inviteAccountId);
        } else {
            initFinanceReq.setParentId(0L);
        }
        financePurseApi.initFinance(initFinanceReq);

        return new IdentityRegisterRes(null, accountId);
    }

    /**
     * 供应商不支持代理注册
     *
     * <p>迁移说明: 旧实现为 {@code void proxyRegister(String)} 空方法体(静默无操作)。
     * 新签名要求返回 {@code IdentityRegisterRes}, 无法保留"空实现"语义,
     * 故显式抛出不支持异常, 避免调用方误判为注册成功。</p>
     *
     * @param proxySaveReq 代理注册参数
     * @return 不会正常返回
     * @author KC
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {
        throw new PlatformException(BaseErrorCode.CUSTOM, "供应商不支持代理注册");
    }


    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {
        // 供应商无法邀请人
        throw new PlatformException(AccountErrorCode.NO_INVITE);
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
