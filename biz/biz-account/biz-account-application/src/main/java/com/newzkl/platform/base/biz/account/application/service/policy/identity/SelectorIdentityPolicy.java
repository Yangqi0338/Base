package com.newzkl.platform.base.biz.account.application.service.policy.identity;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinancePurseApi;
import com.newzkl.platform.base.common.ddd.facade.InitFinanceReq;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.SelectorCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.web.SelectorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.res.AccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 甄选师角色策略
 * @date 2024/1/911:42
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SelectorIdentityPolicy extends AbsIdentityPolicy {

    private final OperatorClientDomain selectorDomain;
    private final UserQueryService roleQueryAppService;
    private final FinancePurseApi financePurseApi;

    @Override
    public RoleEnum.CompanyRole support() {
        return RoleEnum.CompanyRole.SELECTOR;
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
        SelectorVO selectorVO = roleQueryAppService.selectorVO(accountId);
        if (selectorVO != null) {
            if (RoleEnum.State.DESTROY.getCode().equals(selectorVO.getState())) {
                isRegisterOnce = true;
            } else {
                return new IdentityRegisterRes(AccountErrorCode.EXIST_ROLE, accountId);
            }
        }
        //如果已有供应商角色则不能注册其他角色
        if (CommonUtil.strToLongList(account.getRoleIdList()).contains(RoleEnum.CompanyRole.SUPPLIER.getCode().longValue())) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "该手机号已有供应商角色, 请更换手机号");
        }
        //获取邀请人信息
        Long inviteAccountId = customSaveReq.getPid();
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
        }
        //注册角色
        SelectorCustomSaveReq selectorCustomSaveReq = TransferUtils.transfer(customSaveReq, SelectorCustomSaveReq::new);
        selectorCustomSaveReq.setId(accountId);
        selectorCustomSaveReq.setInviteId(inviteAccountId);
        selectorDomain.selectorCustomSave(selectorCustomSaveReq, isRegisterOnce);
        //账号添加角色
        accountService.roleAddEvent(account, inviteAccountVO, RoleEnum.CompanyRole.SELECTOR, customSaveReq.getPassword());
        //邀请成功通知
//        if (!isRegisterOnce && !account.isOld() && inviteAccountId != null) {
//            selectorDomain.inviteSuccess(inviteAccountId, RoleEnum.CompanyRole.SELECTOR, accountId);
//        }
        //初始化财务
        if (!isRegisterOnce) {
            InitFinanceReq initFinanceReq = new InitFinanceReq();
            initFinanceReq.setAccountId(accountId);
            initFinanceReq.setAccountName(customSaveReq.getUsername());
            initFinanceReq.setFinanceUser(PurseEnum.FinanceUser.PICK);
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
        // 迁移说明: 旧实现为 JSONObject.parseObject(registerCommand, SelectorProxySaveReq.class),
        // 入参由 String JSON 改为强类型对象后, 改用 TransferUtils 做同名属性拷贝, 语义等价
        SelectorProxySaveReq selectorProxySaveReq = TransferUtils.transfer(proxySaveReq, SelectorProxySaveReq::new);
        //获取并注册账号
        AccountRegisterRes account = AbsAccountPolicySupport.getPolicy(support().getClient())
                .customRegister(AccountCustomSaveReq.proxyRegister(selectorProxySaveReq.getUsername()));
        Long accountId = account.getId();
        //判断之前是否注册过该角色
        boolean isRegisterOnce = false;
        //检查角色
        SelectorVO selectorVO = roleQueryAppService.selectorVO(accountId);
        if (selectorVO != null) {
            if (RoleEnum.State.DESTROY.getCode().equals(selectorVO.getState())) {
                isRegisterOnce = true;
            } else {
                throw new PlatformException(AccountErrorCode.EXIST_ROLE);
            }
        }
        //获取邀请人信息
        Long inviteAccountId = null;
        AccountVO inviteAccountVO = roleQueryAppService.accountByYqm(selectorProxySaveReq.getYqm());
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
        selectorDomain.selectorProxySave(selectorProxySaveReq, inviteAccountId, accountId);
        //账号添加角色
        accountService.roleAddEvent(account, RoleEnum.CompanyRole.DEALER, selectorProxySaveReq.getPassword());
        //运营商交易师数量更新
        //邀请成功通知
//        if (!isRegisterOnce && !account.isOld() && inviteAccountId != null) {
//            selectorDomain.inviteSuccess(inviteAccountId, RoleEnum.CompanyRole.SELECTOR, accountId);
//        }
        //初始化财务
        if (!isRegisterOnce) {
            InitFinanceReq initFinanceReq = new InitFinanceReq();
            initFinanceReq.setAccountId(accountId);
            initFinanceReq.setAccountName(selectorProxySaveReq.getUsername());
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
    public void roleApplyAuditEvent(AuditEvent auditEvent) {
    }

    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {
        List<EditColumnVO> editColumnList = CollUtil.newArrayList();
        if (!(roleObj instanceof AccountRes)) {
            editColumnList.add(new EditColumnVO("team_count", 1));
        }
        if (roleObj instanceof SelectorVO) {
            editColumnList.add(new EditColumnVO("team_selector_count", 1));
        }
        if (CollectionUtil.isNotEmpty(editColumnList)) {
            addColumn(CollUtil.newArrayList(
                    new EditColumnVO("team_selector_count", 1),
                    new EditColumnVO("team_count", 1)
            ), inviteAccount.getId());
        }
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
