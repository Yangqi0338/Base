package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountSaveReq;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountInfo;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.common.core.sms.VerificationCodeReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountStructureTreeVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// TODO[infra-port sms]: SmsMethod 属 infra, domain 应经 SmsSenderPort; 原 import com.zkl.scm.domain.sms.SmsMethod;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:47
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDomainImpl implements AccountDomain {

    private final AccountRepository accountRepository;
    private final AccountAssembler accountAssembler;


    private static List<String> findSameClientOldRoleId(String roleIdListStr, RoleEnum.CompanyRole newRole) {
        // 从roleEnum中获取新roleId的client
        // 再返回roleIdListStr中同client的所有角色
        if (newRole == null) {
            return new ArrayList<>();
        }
        CommonEnum.Client client = newRole.getClient();
        // 从roleIdListStr中获取同client的所有角色
        return CommonUtil.strToLongList(roleIdListStr)
                .stream()
                .map(RoleEnum.CompanyRole::getByCode)
                .filter(ObjUtil::isNotNull)
                .filter(roleEnum -> client.equals(roleEnum.getClient()))
                .map(it -> it.getCodeStr())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean accountEdit(AccountReq req) {
        AccountVO account = accountAssembler.req2VO(req);

        // 修改account基础信息
        boolean updated = accountRepository.accountEdit(account, null);

        // 角色特定修改
        AbsIdentityPolicySupport.getPolicy(req.getRole()).saveByAccount(req);
        return updated;
    }

    @Override
    public void accountDelete(List<Long> accountIdList) {
        accountRepository.accountDelete(accountIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleAddEvent(AccountRegisterRes account, RoleEnum.CompanyRole role, String password) {
        roleAddEvent(account, null, role, password);
    }

    private void sameClientHandle(AccountVO accountEdit, AccountRegisterRes account, RoleEnum.CompanyRole role, String password) {
        String roleIdList = accountEdit.getRoleIdList();
        List<String> oldRoleIdList = findSameClientOldRoleId(roleIdList, role);
        if (CollUtil.isNotEmpty(oldRoleIdList)) {
            roleIdList = StrUtil.split(roleIdList, ",").stream().map(oldRoleId ->
                    oldRoleIdList.contains(oldRoleId) ? role.getCodeStr() : oldRoleId
            ).distinct().collect(Collectors.joining(","));
        } else {
            roleIdList = roleIdList + "," + role.getCodeStr();
        }

        accountEdit.setRoleIdList(roleIdList);
        if (StrUtil.isNotEmpty(password)) {
            accountEdit.setPassword(new BCryptPasswordEncoder().encode(password));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void roleAddEvent(AccountRegisterRes account, AccountVO inviteAccountVO, RoleEnum.CompanyRole role, String password) {
        if (account == null || account.getId() == null) {
            return;
        }
        Long accountId = account.getId();

        AccountVO accountEdit = new AccountVO();
        accountEdit.setRoleIdList(account.getRoleIdList());
        // 设置层级关系
        accountEdit.setId(accountId);
        // 已经有父的不修改,以防多父
        if (inviteAccountVO != null && account.getPid() != null) {
            Long inviteAccountId = inviteAccountVO.getId();
            String inviteAccountRoleId = inviteAccountVO.getRoleIdList();
            // 不存在邀请人或邀请人是自己
            if (inviteAccountId.equals(accountId)) {
                throw new PlatformException(AccountErrorCode.PARAM_YQM);
            }
            accountEdit.setPid(inviteAccountId);
            accountEdit.setPidList(BizUtil.getPidList(inviteAccountVO.getPidList(), accountEdit.getPid()));
            accountEdit.setPRoleList(BizUtil.getPRoleList(inviteAccountVO.getPRoleList(), inviteAccountRoleId));
            accountEdit.setInviteAccountId(accountEdit.getPid());
        }

        sameClientHandle(accountEdit, account, role, password);

        TransferUtils.transfer(accountEdit, account, CopyOptions.create().ignoreNullValue());
        accountRepository.accountEdit(accountEdit, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void destroy(Long accountId, DestroyRoleReq destroyRoleReq) {
        RoleEnum.CompanyRole role = destroyRoleReq.getRole();
        CommonEnum.Client client = role.getClient();
        AccountVO accountVO = account(client, accountId);
        if (AccountEnum.State.DESTROY == accountVO.getState()) {
            throw new PlatformException(AccountErrorCode.IS_LOCK);
        }

        String username = accountVO.getUsername();
        // 验证验证码
        VerificationCodeReq verificationCodeReq = new VerificationCodeReq();
        verificationCodeReq.setCode(destroyRoleReq.getCode());
        verificationCodeReq.setPhone(username);
        verificationCodeReq.setType(SmsEnum.Type.DESTROY_USER);
        accountRepository.verificationCode(verificationCodeReq);

        // 执行账号注销
        boolean destroyed = accountRepository.destroy(accountVO, destroyRoleReq.getRole());

        // 执行角色注销
        if (destroyed) {
            AbsIdentityPolicySupport.getPolicy(role).destroy(accountVO, destroyRoleReq.getDestroyReason());
            //注销成功通知
//            CodeReq codeReq = new CodeReq();
//            codeReq.setPhone(username);
//            codeReq.setType(SmsEnum.Type.DESTROY_USER_EVENT);
            // TODO[infra-port sms]: SmsMethod.sendCode(codeReq);
//            SmsMethod.send()
        }
    }

    @Override
    public List<AccountStructureTreeVO> findScopeSubAccountStructure(CommonEnum.Client client, Long accountId) {
        AccountParentQuery accountParentQuery = new AccountParentQuery();
        accountParentQuery.setId(accountId);
        accountParentQuery.setClient(client);
//        return subAccountList(accountParentQuery);
        return null;
    }

    @Override
    public @NotNull AccountVO account(CommonEnum.Client client, Long accountId) {
        AccountVO accountVO = accountRepository.account(client, accountId);
        if (accountVO == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        return accountVO;
    }

    @Override
    public AccountInfo accountInfo(AccountQuery query) {
        return accountRepository.getOne(query, AccountInfo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountVO customSave(AccountSaveReq customSaveReq) {
        AccountVO account = new AccountVO();
        account.init(Collections.singletonList(customSaveReq.getRole()), customSaveReq.getUsername(), customSaveReq.getPassword(),
                null, customSaveReq.getState());

        // 同步pid和inviteId的主次数据关系
        account.setPhone(customSaveReq.getPhone());
        account.setUserAccount(customSaveReq.getUserAccount());
        account.setNickname(customSaveReq.getNickname());
        account.setHead(customSaveReq.getHeadImg());
        accountRepository.accountSave(account);
        return account;
    }

    @Override
    public List<AccountVO> accountList(AccountQuery accountQuery) {
        return accountRepository.accountList(accountQuery);
    }

    @Override
    public Page<SimpleAccountRes> simpleAccountPage(SimpleAccountQuery accountQuery) {
        AccountQuery query = accountAssembler.simpleQuery2Query(accountQuery);
        return accountRepository.pageObj(query, SimpleAccountRes.class);
    }

    @Override
    public Page<AccountVO> accountPage(AccountQuery accountQuery) {
        return accountRepository.accountPage(accountQuery);
    }

}
