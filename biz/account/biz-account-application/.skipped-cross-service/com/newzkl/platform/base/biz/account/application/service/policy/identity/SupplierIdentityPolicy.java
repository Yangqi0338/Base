package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zkl.scm.domain.sms.SmsMethod;
import com.zkl.scm.finance.model.purse.req.InitFinanceReq;
import com.zkl.scm.finance.rpc.facade.purse.IPurseFacade;
import com.newzkl.platform.base.biz.account.model.support.CodeReq;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.biz.account.model.enums.SmsEnum;
import com.newzkl.platform.base.biz.account.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.policy.AbsAccountPolicySupport;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.AuditRoleApplyVO;
import com.newzkl.platform.base.biz.account.model.vo.NameAuthVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.zkl.scm.user.model.relation.req.AccountLevelUpReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.spring.annotation.GlobalTransactional;
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

    @DubboReference
    private IPurseFacade accountPurseApi;
    private final UserQueryService roleQueryAppService;
    private final SupplierClientDomain supplierDomain;
    private final OperatorClientDomain operatorDomain;
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
                .customRegister(customSaveReq);
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
                throw new ScmException(AccountErrorCode.PARAM_YQM);
            } else {
//                log.info("邀请人身份ID：{}", inviteAccountVO.getSubRoleIdList());
                if (!(inviteAccountVO.getRoleIdList().contains(RoleEnum.CompanyRole.SELECTOR.getCodeStr()) ||
                        inviteAccountVO.getRoleIdList().contains(RoleEnum.CompanyRole.DEALER.getCodeStr()) ||
                        inviteAccountVO.getRoleIdList().contains(RoleEnum.CompanyRole.OPERATOR.getCodeStr()))) {
                    throw new ScmException(AccountErrorCode.NO_INVITE);
                } //只有交易师、渠道商、运营商可邀请供应商
               /* if(!(inviteAccountVO.getRoleIdList().contains(RoleEnum.CompanyRole.SELECTOR.getCode().toString())|| inviteAccountVO.getRoleIdList().contains(RoleEnum.CompanyRole.DEALER.getCode().toString())) || inviteAccountVO.getRoleIdList().contains(RoleEnum.CompanyRole.OPERATOR.getCode().toString())){
                    throw new ScmException(AccountErrorCode.NO_INVITE);
                }*/
                else {
                    inviteAccountId = inviteAccountVO.getId();
                }
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
        accountPurseApi.initFinance(initFinanceReq);
        //邀请成功通知
        if (inviteAccountId != null) {
            operatorDomain.inviteSuccess(inviteAccountId, RoleEnum.CompanyRole.SUPPLIER, accountId);
        }

        return new IdentityRegisterRes(null, accountId);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq) {

    }



    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void roleApplyAuditEvent(AuditEvent auditEvent) {
        Integer state = auditEvent.getState();
        Long accountId = auditEvent.getAccountId();
        String data = auditEvent.getData();
        AuditRoleApplyVO auditRoleApplyDataVO = JSON.parseObject(data, AuditRoleApplyVO.class);
        AccountVO accountVO = accountDomain.account(null, accountId);
        //成功处理
        if (AuditEnum.State.SUCCESS.getCode().equals(state)) {
            String companyInfo = auditRoleApplyDataVO.getCompanyInfo();
            String nameAuthInfo = auditRoleApplyDataVO.getNameAuthInfo();
            //创建角色
            supplierDomain.auditPass(accountVO, companyInfo);
            //个人账号处理
            if (StringUtils.isNotEmpty(nameAuthInfo)) {
                NameAuthVO nameAuthVO = JSONObject.parseObject(nameAuthInfo, NameAuthVO.class);
                accountDomain.nameAuthSuccess(accountId, nameAuthVO);
            }
            //角色计数
//            roleDomain.addCount(auditRoleApplyDataVO.getRoleId().longValue());
        } else if (AuditEnum.State.FAIL.getCode().equals(state)) {
            supplierDomain.auditFail(accountId, auditEvent.getLastRefuseReason());
        } else {
            log.error("用户消费(审批:保证金缴纳)事件: 无法识别消息的审批状态");
            throw new ScmException(BaseErrorCode.PARAM);
        }
        //发送短信
        CodeReq codeReq = new CodeReq();
        codeReq.setPhone(auditRoleApplyDataVO.getRegisterPhone());
        if (AuditEnum.State.SUCCESS.getCode().equals(state)) {
            codeReq.setType(SmsEnum.Type.SUPPLIER_AUDIT_SUCCESS);
            SmsMethod.sendCode(codeReq);
        } else if (AuditEnum.State.FAIL.getCode().equals(state)) {
            RoleEnum.CompanyRole companyRole = auditRoleApplyDataVO.getRole();
            codeReq.setType(SmsEnum.Type.SUPPLIER_AUDIT_FAIL);
            //codeReq.setParams(Arrays.asList(companyRole.getValue(), auditEvent.getLastRefuseReason()));
            SmsMethod.sendCode(codeReq);
        } else {
            log.error("用户消费(审批:保证金缴纳)事件: 无法识别消息的审批状态");
            throw new ScmException(BaseErrorCode.PARAM);
        }
    }


    @Override
    public void inviteSuccess(AccountVO inviteAccount, AccountVO account, Object roleObj) {
        // 供应商无法邀请人
        throw new ScmException(AccountErrorCode.NO_INVITE);
    }

    @Override
    public Pair<RoleEnum.CompanyRole, Double> levelUpCheck(AccountLevelUpReq levelUpReq) {
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
