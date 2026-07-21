package com.newzkl.platform.base.biz.account.application.service.policy.identity;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.zkl.scm.finance.model.purse.req.InitFinanceReq;
import com.zkl.scm.finance.rpc.facade.purse.IPurseFacade;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.biz.account.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
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
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentitySaveReq;
import com.zkl.scm.user.model.relation.req.AccountLevelUpReq;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @DubboReference
    private IPurseFacade purseFacade;

    @Override
    public RoleEnum.CompanyRole support() {
        return RoleEnum.CompanyRole.DEALER;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(customSaveReq);
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
            throw new ScmException(BaseErrorCode.CUSTOM, "该手机号已有供应商角色, 请更换手机号");
        }
        //获取邀请人信息
        Long inviteAccountId = null;
        AccountVO inviteAccountVO = roleQueryAppService.accountByYqm(customSaveReq.getYqm());
        if (inviteAccountVO != null) {
            if (accountId.equals(inviteAccountVO.getId())) {
                throw new ScmException(AccountErrorCode.PARAM_YQM);
            } else {
                if (CollUtil.isEmpty(RoleEnumUtil.getOperatorLevelUpEnumList(inviteAccountVO.getRoleIdList()))) {
                    throw new ScmException(AccountErrorCode.NO_INVITE);
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
                operatorDomain.operatorEdit(Collections.singletonList(new EditColumnDTO("dealer_number", 1)), inviteAccountId);
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
            purseFacade.initFinance(initFinanceReq);
        }
        return new IdentityRegisterRes(null, accountId);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {
        DealerProxySaveReq dealerProxySaveReq = JSONUtil.toBean(proxySaveReq, DealerProxySaveReq.class);
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(IdentitySaveReq.proxyRegister(dealerProxySaveReq.getUsername()));
        Long accountId = account.getId();
        //判断之前是否注册过该角色
        boolean isRegisterOnce = false;
        //检查角色
        DealerVO dealerVO = roleQueryAppService.dealerVO(accountId);
        if (dealerVO != null) {
            if (RoleEnum.State.DESTROY.getCode().equals(dealerVO.getState())) {
                isRegisterOnce = true;
            } else {
                throw new ScmException(AccountErrorCode.EXIST_ROLE);
            }
        }
        //获取邀请人信息
        Long inviteAccountId = null;
        AccountVO inviteAccountVO = roleQueryAppService.accountByYqm(dealerProxySaveReq.getYqm());
        if (inviteAccountVO != null) {
            if (accountId.equals(inviteAccountVO.getId())) {
                throw new ScmException(AccountErrorCode.PARAM_YQM);
            } else {
                if (CollUtil.isEmpty(RoleEnumUtil.getOperatorLevelUpEnumList(inviteAccountVO.getRoleIdList()))) {
                    throw new ScmException(AccountErrorCode.NO_INVITE);
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
                operatorDomain.operatorEdit(Collections.singletonList(new EditColumnDTO("dealer_number", 1)), inviteAccountId);
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
            purseFacade.initFinance(initFinanceReq);
        }
    }

    @Override
    public void roleApplyAuditEvent(AuditEvent auditEvent) {

    }



    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {
        // 交易师邀人逻辑
    }

    @Override
    public boolean levelUp(AccountLevelUpReq levelUpReq, AccountVO account) {
//        CustomSaveReq customSaveReq = TransferUtils.transfer(account, accountAssembler::do2SaveReq);
        IdentitySaveReq customSaveReq = null;
        customSaveReq.setEncodePassword(account.getPassword());
        IdentityRegisterRes registerRes = customRegister(customSaveReq);
        return registerRes != null;
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
