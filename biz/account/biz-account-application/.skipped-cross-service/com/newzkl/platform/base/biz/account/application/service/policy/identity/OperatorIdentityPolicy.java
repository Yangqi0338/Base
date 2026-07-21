package com.newzkl.platform.base.biz.account.application.service.policy.identity;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.zkl.scm.finance.model.purse.req.InitFinanceReq;
import com.zkl.scm.finance.rpc.facade.purse.IPurseFacade;
import com.zkl.scm.goods.rpc.facade.IStoreFacade;
import com.zkl.scm.goods.rpc.model.store.StoreRegisterReq;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.ChannelEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.OperatorEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.OperatorCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.web.OperatorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.res.DealerRes;
import com.newzkl.platform.base.biz.account.model.res.OperatorInfo;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentitySaveReq;
import com.zkl.scm.user.model.relation.req.AccountLevelUpReq;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author muc_fang
 * @Description: 运营商角色策略
 * @date 2024/1/911:42
 */
@Component
@RequiredArgsConstructor
public class OperatorIdentityPolicy extends AbsIdentityPolicy {

    @DubboReference
    private IPurseFacade purseFacade;
    private final OperatorClientDomain operatorDomain;
    private final UserQueryService roleQueryAppService;
    @DubboReference
    private IStoreFacade storeFacade;

    @Override
    public RoleEnum.CompanyRole support() {
        return RoleEnum.CompanyRole.OPERATOR;
    }

    @Override
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(customSaveReq);
        Long accountId = account.getId();
        //检查角色
        OperatorVO operatorVO = roleQueryAppService.operatorVO(accountId);
        //判断之前是否注册过该角色
        if (operatorVO != null) {
            return new IdentityRegisterRes(AccountErrorCode.EXIST_ROLE, accountId);
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
        }
        //注册角色
        OperatorCustomSaveReq operatorEditReq = new OperatorCustomSaveReq();
        operatorEditReq.setUsername(customSaveReq.getUsername());
        operatorEditReq.setName(customSaveReq.getNickname());
        operatorEditReq.setId(accountId);
        operatorEditReq.setInviteId(inviteAccountId);
        operatorEditReq.setHeadImg(customSaveReq.getHeadImg());
        operatorDomain.operatorCustomSave(operatorEditReq, accountId);
        //账号添加角色
        accountService.roleAddEvent(account, inviteAccountVO, RoleEnum.CompanyRole.OPERATOR, customSaveReq.getPassword());
        //初始化财务
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
        return new IdentityRegisterRes(null, accountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {
        OperatorProxySaveReq operatorProxySaveReq = JSONUtil.toBean(proxySaveReq, OperatorProxySaveReq.class);
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(IdentitySaveReq.proxyRegister(operatorProxySaveReq.getUsername()));
        Long accountId = account.getId();
        //检查角色
        OperatorVO operatorVO = roleQueryAppService.operatorVO(accountId);
        if (operatorVO != null) {
            throw new ScmException(AccountErrorCode.EXIST_ROLE);
        }
        //如果已有供应商角色则不能注册其他角色
        if (CommonUtil.strToLongList(account.getRoleIdList()).contains(RoleEnum.CompanyRole.SUPPLIER.getCode().longValue())) {
            throw new ScmException(BaseErrorCode.CUSTOM, "该手机号已有供应商角色, 请更换手机号");
        }

        //注册运营商
        operatorDomain.operatorProxySave(operatorProxySaveReq, accountId);
        if (OperatorEnum.Type.BRAND.equals(operatorProxySaveReq.getType())) {
            IdentitySaveReq customSaveReq = new IdentitySaveReq();

            OperatorInfo operatorInfo = JSONUtil.toBean(operatorProxySaveReq.getInfo(), OperatorInfo.class);
            customSaveReq.setUsername(operatorProxySaveReq.getUsername());
            customSaveReq.setPassword(operatorProxySaveReq.getPassword());
            customSaveReq.setName(operatorProxySaveReq.getName());
            customSaveReq.setOperatorMark(CommonEnum.YesOrNo.YES);
            customSaveReq.setRegisterDomain(operatorProxySaveReq.getDomain());
            customSaveReq.setBodyType(AccountEnum.BodyType.COMPANY);
            customSaveReq.setContactsWay(operatorProxySaveReq.getPhone());
            customSaveReq.setStoreName(operatorProxySaveReq.getName());
            customSaveReq.setContactsName(operatorProxySaveReq.getName());
            customSaveReq.setCompanyInfo(new IdentitySaveReq.CompanyInfo(operatorInfo.getCompanyName()));
            customSaveReq.setStorePermission(CommonEnum.YesOrNo.YES);
            customSaveReq.setChannelType(ChannelEnum.ChannelType.STORE);
            AbsIdentityPolicySupport.getPolicy(RoleEnum.CompanyRole.CHANNEL).customRegister(customSaveReq);

            // 重新赋值account, 避免渠道商的账号修改被覆盖
//            account = accountService.account(support().getClient(), accountId);

            StoreRegisterReq storeRegisterReq = new StoreRegisterReq(accountId);
            storeRegisterReq.setStoreName(operatorProxySaveReq.getTypeForeignName());
            storeFacade.openStore(storeRegisterReq);
        }
        //账号添加角色
        accountService.roleAddEvent(account, RoleEnum.CompanyRole.OPERATOR, operatorProxySaveReq.getPassword());
        //初始化财务
        InitFinanceReq initFinanceReq = new InitFinanceReq();
        initFinanceReq.setAccountId(accountId);
        initFinanceReq.setAccountName(operatorProxySaveReq.getUsername());
        initFinanceReq.setFinanceUser(PurseEnum.FinanceUser.OPERATOR);
        initFinanceReq.setParentId(AccountEnum.MAIN_ACCOUNT_PID);
        initFinanceReq.setSubPurseType(null);
        if (ObjectUtil.isNotNull(operatorProxySaveReq.getBalanceType()) && operatorProxySaveReq.getBalanceType() == 1) {
            initFinanceReq.setLeverageRatio(operatorProxySaveReq.getLeverageRatio());
        }
        purseFacade.initFinance(initFinanceReq);

    }

    @Override
    public void roleApplyAuditEvent(AuditEvent auditEvent) {

    }

    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {
        // 运营商邀人逻辑
        if (roleObj instanceof DealerRes) {
            addColumn(CollUtil.newArrayList(new EditColumnDTO("dealer_number", 1)), inviteAccount.getId());
        }
    }

    @Override
    public boolean levelUp(AccountLevelUpReq levelUpReq, AccountVO account) {
//        CustomSaveReq customSaveReq = TransferUtils.transfer(account, accountAssembler::do2SaveReq);
        IdentitySaveReq customSaveReq = null;
        customSaveReq.setEncodePassword(account.getPassword());
        //获取并注册账号
        Long accountId = account.getId();
        //检查角色
        OperatorVO operatorVO = roleQueryAppService.operatorVO(accountId);
        if (operatorVO != null) {
            throw new ScmException(AccountErrorCode.EXIST_ROLE);
        }
        //如果已有供应商角色则不能注册其他角色
        if (account.getRoleIdList().contains(RoleEnum.CompanyRole.SUPPLIER.getCodeStr())) {
            throw new ScmException(BaseErrorCode.CUSTOM, "该手机号已有供应商角色, 请更换手机号");
        }

        //注册运营商
        // 无初始信息
        OperatorCustomSaveReq operatorCustomSaveReq = new OperatorCustomSaveReq();
        operatorCustomSaveReq.setName(account.getRealName());
        operatorCustomSaveReq.setUsername(account.getUsername());
        operatorCustomSaveReq.setBalanceType(0);
        operatorCustomSaveReq.setLeverageRatio(0);
        operatorDomain.operatorCustomSave(operatorCustomSaveReq, accountId);
        //账号添加角色
//        accountService.roleAddEvent(account, RoleEnum.CompanyRole.OPERATOR, customSaveReq.getPassword(), customSaveReq.getEncodePassword());
        //初始化财务
        InitFinanceReq initFinanceReq = new InitFinanceReq();
        initFinanceReq.setAccountId(accountId);
        initFinanceReq.setAccountName(account.getUsername());
        initFinanceReq.setFinanceUser(PurseEnum.FinanceUser.OPERATOR);
        initFinanceReq.setParentId(AccountEnum.MAIN_ACCOUNT_PID);
        initFinanceReq.setSubPurseType(null);
        if (ObjectUtil.isNotNull(operatorCustomSaveReq.getBalanceType()) && operatorCustomSaveReq.getBalanceType() == 1) {
            initFinanceReq.setLeverageRatio(operatorCustomSaveReq.getLeverageRatio());
        }
        purseFacade.initFinance(initFinanceReq);
        return true;
    }

    @Override
    public Pair<RoleEnum.CompanyRole, Double> levelUpCheck(AccountLevelUpReq levelUpReq) {
        return null;
    }

    @Override
    public Integer findSupplierGoodsCount(Long accountId) {
        OperatorVO operator = operatorDomain.operator(accountId);
        if (operator == null) {
            return 0;
        }
        return operator.getSupplierGoodsCount();
    }

    @Override
    public Integer getOrderTotalAmount(Long accountId) {
        return 0;
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
