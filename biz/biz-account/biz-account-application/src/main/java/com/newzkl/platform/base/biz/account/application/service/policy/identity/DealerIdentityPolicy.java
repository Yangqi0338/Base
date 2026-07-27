package com.newzkl.platform.base.biz.account.application.service.policy.identity;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinancePurseApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.InitFinanceReq;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.DealerCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.web.DealerProxySaveReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author muc_fang
 * @Description: 交易师角色策略
 * @date 2024/1/911:42
 */
@Component
@RequiredArgsConstructor
public class DealerIdentityPolicy extends AbsIdentityPolicy {

    private final OperatorClientDomain operatorDomain;
    private final UserQueryService roleQueryAppService;
    private final FinancePurseApi financePurseApi;

    @Override
    public RoleEnum.CompanyRole support() {
        return RoleEnum.CompanyRole.DEALER;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(identityAssembler.identityCustomReq2AccountCustomReq(customSaveReq));
        Long accountId = account.getId();
        //判断之前是否注册过该角色
        boolean isRegisterOnce = false;
        //检查角色
        DealerVO dealerVO = roleQueryAppService.dealerVO(accountId);
        if (dealerVO != null) {
            if (RoleEnum.State.DESTROY.getCode().equals(dealerVO.getState())) {
                isRegisterOnce = true;
            } else {
                return new IdentityRegisterRes(AccountErrorCode.EXIST_ROLE, accountId);
            }
        }
        //如果已有供应商角色则不能注册其他角色
        if (account.getRoleIdList().contains(RoleEnum.CompanyRole.SUPPLIER.getCodeStr())) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "该手机号已有供应商角色, 请更换手机号");
        }
        //获取邀请人信息
        Long inviteAccountId = null;
        AccountVO inviteAccountVO = roleQueryAppService.accountByYqm(customSaveReq.getYqm());
        if (inviteAccountVO != null) {
            if (accountId.equals(inviteAccountVO.getId())) {
                throw new PlatformException(AccountErrorCode.PARAM_YQM);
            } else {
                if (CollUtil.isEmpty(RoleEnumUtil.getOperatorLevelUpEnumList(inviteAccountVO.getRoleIdList()))) {
                    throw new PlatformException(AccountErrorCode.NO_INVITE);
                } else {
                    inviteAccountId = inviteAccountVO.getId();
                }
            }
        } else {
            inviteAccountId = 497000000100000L;
        }
        //注册角色
        DealerCustomSaveReq dealerEditReq = new DealerCustomSaveReq();
        dealerEditReq.setUsername(customSaveReq.getUsername());
        dealerEditReq.setName(customSaveReq.getNickname());
        dealerEditReq.setId(accountId);
        dealerEditReq.setOperatorId(inviteAccountId);
        dealerEditReq.setHeadImg(customSaveReq.getHeadImg());
        operatorDomain.dealerCustomSave(dealerEditReq, isRegisterOnce);
        //账号添加角色
        accountService.roleAddEvent(account, inviteAccountVO, RoleEnum.CompanyRole.DEALER, customSaveReq.getPassword());
        //运营商交易师数量更新
        if (!isRegisterOnce) {
            if (inviteAccountId != null) {
                operatorDomain.operatorEdit(Collections.singletonList(new EditColumnVO("dealer_number", 1)), inviteAccountId);
            }
        }
        //初始化财务
        if (!isRegisterOnce) {
            InitFinanceReq initFinanceReq = new InitFinanceReq();
            initFinanceReq.setAccountId(accountId);
            initFinanceReq.setAccountName(customSaveReq.getUsername());
            initFinanceReq.setFinanceUser(PurseEnum.FinanceUser.TRADERS);
            initFinanceReq.setSubPurseType(null);
            if (inviteAccountId != null) {
                initFinanceReq.setParentId(inviteAccountId);
            } else {
                initFinanceReq.setParentId(0L);
            }
            financePurseApi.initFinance(initFinanceReq);
        }
        return new IdentityRegisterRes(null, accountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {
        // 迁移说明: 旧实现为 JSONObject.parseObject(registerCommand, DealerProxySaveReq.class),
        // 入参由 String JSON 改为强类型对象后, 改用 TransferUtils 做同名属性拷贝, 语义等价
        DealerProxySaveReq dealerProxySaveReq = TransferUtils.transfer(proxySaveReq, DealerProxySaveReq::new);
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(AccountCustomSaveReq.proxyRegister(dealerProxySaveReq.getUsername()));
        Long accountId = account.getId();
        //判断之前是否注册过该角色
        boolean isRegisterOnce = false;
        //检查角色
        DealerVO dealerVO = roleQueryAppService.dealerVO(accountId);
        if (dealerVO != null) {
            if (RoleEnum.State.DESTROY.getCode().equals(dealerVO.getState())) {
                isRegisterOnce = true;
            } else {
                throw new PlatformException(AccountErrorCode.EXIST_ROLE);
            }
        }
        //获取邀请人信息
        Long inviteAccountId = null;
        AccountVO inviteAccountVO = roleQueryAppService.accountByYqm(dealerProxySaveReq.getYqm());
        if (inviteAccountVO != null) {
            if (accountId.equals(inviteAccountVO.getId())) {
                throw new PlatformException(AccountErrorCode.PARAM_YQM);
            } else {
                if (CollUtil.isEmpty(RoleEnumUtil.getOperatorLevelUpEnumList(inviteAccountVO.getRoleIdList()))) {
                    throw new PlatformException(AccountErrorCode.NO_INVITE);
                } else {
                    inviteAccountId = inviteAccountVO.getId();
                }
            }
        }
        //注册角色
        operatorDomain.dealerProxySave(dealerProxySaveReq, inviteAccountId, accountId, isRegisterOnce);
        //账号添加角色
        accountService.roleAddEvent(account, RoleEnum.CompanyRole.DEALER, dealerProxySaveReq.getPassword());
        //运营商交易师数量更新
        if (!isRegisterOnce) {
            if (inviteAccountId != null) {
                operatorDomain.operatorEdit(Collections.singletonList(new EditColumnVO("dealer_number", 1)), inviteAccountId);
            }
        }
        //初始化财务
        if (!isRegisterOnce) {
            InitFinanceReq initFinanceReq = new InitFinanceReq();
            initFinanceReq.setAccountId(accountId);
            initFinanceReq.setAccountName(dealerProxySaveReq.getUsername());
            initFinanceReq.setFinanceUser(PurseEnum.FinanceUser.TRADERS);
            initFinanceReq.setSubPurseType(null);
            if (inviteAccountId != null) {
                initFinanceReq.setParentId(inviteAccountId);
            } else {
                initFinanceReq.setParentId(0L);
            }
            financePurseApi.initFinance(initFinanceReq);
        }
        return new IdentityRegisterRes(null, accountId);
    }

    @Override
    public void roleApplyAuditEvent(AuditEvent auditEvent) {

    }



    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {
        // 交易师邀人逻辑
    }

    @Override
    public Integer findSupplierGoodsCount(Long accountId) {
        DealerVO dealer = operatorDomain.dealer(accountId);
        if (dealer == null) {
            return 0;
        }
        return dealer.getSupplierGoodsCount();
    }

    @Override
    public Integer getOrderTotalAmount(Long accountId) {
        DealerVO dealer = operatorDomain.dealer(accountId);
        if (dealer == null) {
            return 0;
        }
        return dealer.getSupplierGoodsCount();
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
