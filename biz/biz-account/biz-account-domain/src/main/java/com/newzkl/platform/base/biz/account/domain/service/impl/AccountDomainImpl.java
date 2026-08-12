package com.newzkl.platform.base.biz.account.domain.service.impl;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// TODO[infra-port sms]: SmsMethod 属 infra, domain 应经 SmsSenderPort; 原 import com.zkl.scm.domain.sms.SmsMethod;
import com.newzkl.platform.base.biz.account.model.support.CodeReq;
import com.newzkl.platform.base.biz.account.model.support.VerificationCodeReq;
import com.newzkl.platform.base.biz.account.model.event.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.sms.enums.SmsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.auth.repository.AccountLoginRepository;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.*;
import com.newzkl.platform.base.biz.account.model.vo.*;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.CodeUpdatePasswordReq;

import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.spring.TransactionUtils;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final AccountLoginRepository accountLoginRepository;
    private final TransactionUtils transactionUtils;


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
        // 修改账号
        if (StrUtil.isNotBlank(req.getUsername())) {
            CodeUpdateUsernameReq usernameReq = new CodeUpdateUsernameReq();
            editUsername(usernameReq);
        }
        // 修改密码
        if (StrUtil.isNotBlank(req.getPassword())) {
            CodeUpdatePasswordReq passwordReq = new CodeUpdatePasswordReq();
            editPassword(passwordReq);
        }

        // 修改account基础信息
        boolean updated = accountRepository.accountEdit(account, null);

        // 角色特定修改
        AbsIdentityPolicySupport.getPolicy(req.getRole()).saveByAccount(req);
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPassword(CodeUpdatePasswordReq req) {
        BizUtil.validate(req);

        CommonEnum.Client client = req.getClient();
        Long accountId = req.getAccountId();
        // TODO 加密字符串修改
        String sign = req.getSign();
        //查询用户信息
        AccountVO account = account(client, accountId);

        //检查验证码
        VerificationCodeReq codeReq = new VerificationCodeReq();
        codeReq.setPhone(account.getPhone());
        codeReq.setCode(req.getCode());
        codeReq.setType(SmsEnum.Type.UpdatePassword);
        accountRepository.verificationCode(codeReq);
        //获取新密码
        String newPassword = account.getNewPassword(req.getNewPassword());
        //持久化
        AccountVO accountEdit = new AccountVO();
        accountEdit.setId(account.getId());
        accountEdit.setPassword(newPassword);
        accountRepository.accountEdit(accountEdit, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUsername(CodeUpdateUsernameReq req) {
        BizUtil.validate(req);

        //查询用户信息
        Long accountId = req.getAccountId();
        CommonEnum.Client client = req.getClient();
        String newUsername = req.getNewUsername();
        AccountVO account = account(client, accountId);

        //校验新账号名称
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setUsername(req.getNewUsername());
        accountQuery.setMainAccountId(account.getMainAccountId());
        accountQuery.setClient(client);
        accountQuery.setState(AccountEnum.State.ENABLE);
        Long newAccountId = accountRepository.findId(accountQuery);
        if (newAccountId != null) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }

        // 若新账号名不是手机号,则使用旧账号的手机号
        String phone = PhoneUtil.isPhone(newUsername) ? newUsername : account.getPhone();
        //校验验证码
        VerificationCodeReq codeReq = new VerificationCodeReq();
        codeReq.setPhone(phone);
        codeReq.setCode(req.getCode());
        codeReq.setType(SmsEnum.Type.UpdateUsername);
        accountRepository.verificationCode(codeReq);

        //持久化
        AccountVO accountUpdate = new AccountVO();
        accountUpdate.setId(account.getId());
        accountUpdate.setUsername(req.getNewUsername());
        accountRepository.accountEdit(accountUpdate, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subEditBase(Long parentId, Long accountId, SubEditReq subEditReq) {
        SubAccount accountEdit = new SubAccount();
        //检查父账号是否具备授权的角色
        AccountVO parentAccount = account(null, parentId);

        if (subEditReq.getRoleIdList().stream().anyMatch(roleId -> !parentAccount.getRoleIdList().contains(roleId + ""))) {
            throw new PlatformException(AccountErrorCode.NOT_OPEN_ROLE);
        }
        //保存账号
        accountEdit.setRoleIdList(CollUtil.join(subEditReq.getRoleIdList(), ","));
        accountEdit.setId(subEditReq.getId());
        accountEdit.setNickname(subEditReq.getNickname());
        accountEdit.setUsername(subEditReq.getUsername());
        accountEdit.setPassword(subEditReq.getPassword());
        accountEdit.setJobIdList(CollUtil.join(subEditReq.getJobIdList(), ","));
        accountRepository.subAccountEdit(accountEdit);
    }

    @Override
    public void nameAuthSubmit(Long accountId, NameAuthVO nameAuthVO) {
        AccountVO account = new AccountVO();
        account.setId(accountId);
        account.setNameAuthAuditState(AuditEnum.State.AUDITING);
        accountRepository.accountEdit(account, null);
    }

    @Override
    public void nameAuthSuccess(Long accountId, NameAuthVO nameAuthVO) {
        AccountVO account = new AccountVO();
        account.setId(accountId);
        account.setRealName(nameAuthVO.getName());
        account.setNameAuthInfo(JSONObject.toJSONString(nameAuthVO));
        account.setNameAuthAuditState(AuditEnum.State.SUCCESS);
        accountRepository.accountEdit(account, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void nameAuthComplete(Long accountId, AuditEvent auditEvent) {
        AuditDataNameAuthVO messageData = JSON.parseObject(auditEvent.getData(), AuditDataNameAuthVO.class);
        if (AuditEnum.State.SUCCESS.getCode().equals(auditEvent.getState())) {
            AccountVO account = new AccountVO();
            account.setId(accountId);
            NameAuthVO nameAuthVO = JSONObject.parseObject(messageData.getNameAuthInfo(), NameAuthVO.class);
            account.setRealName(nameAuthVO.getName());
            account.setNameAuthInfo(messageData.getNameAuthInfo());
            account.setNameAuthAuditState(AuditEnum.State.SUCCESS);
            accountRepository.accountEdit(account, null);
        } else if (AuditEnum.State.FAIL.getCode().equals(auditEvent.getState())) {
            AccountVO account = new AccountVO();
            account.setId(accountId);
            account.setNameAuthAuditState(AuditEnum.State.FAIL);
            accountRepository.accountEdit(account, null);
        } else {
            log.warn("未处理其他事件");
        }
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
            CodeReq codeReq = new CodeReq();
            codeReq.setPhone(username);
            codeReq.setType(SmsEnum.Type.DESTROY_USER_EVENT);
            // TODO[infra-port sms]: SmsMethod.sendCode(codeReq);
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
    public AccountVO proxySave(Long pid, SubProxySaveReq proxySaveReq) {
        //查询父账号信息
        AccountVO account = accountRepository.account(CommonEnum.Client.ADMIN, pid);
        //检查是否与父账号重复
        if (account.getUsername().equals(proxySaveReq.getUsername())) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }
        //检查父账号是否具备授权的角色
        for (Long item : proxySaveReq.getRoleIdList()) {
            List<Long> parentRoleIdList = StrUtil.split(account.getRoleIdList(), ',', -1, true, NumberUtil::parseLong);
            if (!parentRoleIdList.contains(item)) {
                throw new PlatformException(AccountErrorCode.NOT_OPEN_ROLE);
            }
        }
        //检查子账号是否存在
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setUsername(proxySaveReq.getUsername());
        accountQuery.setPid(pid);
        int count = accountRepository.accountCountByQuery(accountQuery);
        if (count > 0) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }
        //保存账号
        SubAccount subAccount = new SubAccount();
        subAccount.setNickname(proxySaveReq.getNickname());
        subAccount.init(proxySaveReq.getRoleIdList(),
                proxySaveReq.getUsername(),
                proxySaveReq.getPassword(),
                null,
                null
        );
        //保存子账号
        accountRepository.subAccountSave(subAccount);
        return account;
    }

    @Override
    public List<AccountVO> accountList(AccountQuery accountQuery) {
        return accountRepository.accountList(accountQuery);
    }

    @Override
    public List<RoleVO> accountRoleList(Long accountId, CommonEnum.Client client) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setAccountId(accountId);
        accountQuery.setClient(client);
        accountQuery.addField(AccountVO::getClient, AccountVO::getRoleIdList);

        List<AccountVO> accountVOList = accountRepository.accountList(accountQuery);

        List<RoleVO> roleList = new ArrayList<>();
        if (CollUtil.isEmpty(accountVOList)) {
            return roleList;
        }

        // 根据端进行分组
        accountVOList.stream().collect(CommonUtil.groupingSingleBy(AccountVO::getClient))
                .forEach((key, accountVO) -> {
                    List<RoleEnum.CompanyRole> companyRoleList = RoleEnumUtil.getLevelUpEnumList(key, accountVO.getRoleIdList());
                    RoleVO roleVO = new RoleVO();
                    roleVO.setRoleList(companyRoleList);
                    roleVO.setRoleName(companyRoleList.stream().map(RoleEnum.CompanyRole::getCodeStr).collect(Collectors.joining("、")));
                    roleVO.setClient(key);
                    if (CommonEnum.Client.OPERATOR == roleVO.getClient()) {
                        roleVO.setShowName("其他身份");
                    } else {
                        roleVO.setShowName(roleVO.getRoleName());
                    }
                    roleList.add(roleVO);
                });

        return roleList;
    }

    public RoleEnum.CompanyRole findRole(AccountVO account) {
        CommonEnum.Client client = SecurityUtils.getClient();
        RoleEnum.CompanyRole companyRole = null;
        if (account != null) {
            if (client == null) {
                // 获取最早的角色
                String earlyRoleId = CollUtil.getFirst(StrUtil.split(account.getRoleIdList(), ","));
                Long roleId = NumberUtil.parseLong(earlyRoleId, null);
                companyRole = RoleEnum.CompanyRole.getByCode(roleId);
            } else {
                companyRole = CollUtil.getLast(RoleEnumUtil.getLevelUpEnumList(client, account.getRoleIdList()));
            }
        }
        return companyRole;
    }

    @Override
    public List<SubAccountVO> subAccountList(AccountParentQuery query) {
        AccountVO accountVO = account(query.getClient(), query.getId());
        if (StrUtil.isBlank(query.getPidList())) {
            query.setPidList(BizUtil.getPidList(accountVO.getPidList(), query.getId()));
        }

        // 查询在运营商端下 所有的下级
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setPidList(BizUtil.getPidList(accountVO.getPidList(), query.getId()));
        accountQuery.setClient(query.getClient());

        List<AccountStructureTreeVO> structureList = accountAssembler.structure2TreeList(accountRepository.listObj(accountQuery, AccountStructureVO.class));

        List<SubAccountVO> resList = new ArrayList<>();
        if (CollUtil.isEmpty(structureList)) {
            return resList;
        }

        // 根据对应的account查出 非运营商体系下其他端的用户
        AccountQuery invitedAccountQuery = new AccountQuery();
        invitedAccountQuery.setInviteAccountIdList(structureList.stream().map(AccountStructureTreeVO::getId).collect(Collectors.toList()));

        List<AccountStructureTreeVO> invitedStructureList = accountAssembler.structure2TreeList(accountRepository.listObj(invitedAccountQuery, AccountStructureVO.class));


        List<AccountStructureTreeVO> newList = AccountStructureTreeVO.buildTotalCount(query.getClient(), AccountStructureTreeVO.buildTree(structureList));
        newList.removeIf(it -> !it.getPid().equals(accountVO.getId()));

        // 根据同client的角色进行分组
        newList.stream().collect(CollectorUtil.groupingBy(subAccount ->
                CollUtil.getLast(RoleEnumUtil.getLevelUpEnumList(query.getClient(), subAccount.getRoleIdList())))
        ).forEach((role, sameRoleSubAccountList) -> {
            if (role != null) {
                sameRoleSubAccountList.forEach(subStructure -> {
                    SubStructureReq subReq1 = new SubStructureReq();
                    subReq1.setPidList(BizUtil.getPidList(subStructure.getPidList(), subStructure.getId()));
                    subReq1.setRoleIdList(query.getRoleIdList());
                    List<AccountStructureVO> subList = accountRepository.findScopeSubAccountStructure(0L);

                    SubAccountVO subAccountVO = accountAssembler.subStructure2AccountVO(subStructure);
                    resList.add(subAccountVO);
                    subAccountVO.setRole(role);
                    subAccountVO.setOperatorCount((int) subList.stream().filter(it -> it.getRoleIdList().contains(RoleEnum.CompanyRole.OPERATOR.getCode().toString())).count());
                    subAccountVO.setSelectorCount((int) subList.stream().filter(it -> it.getRoleIdList().contains(RoleEnum.CompanyRole.SELECTOR.getCode().toString())).count());
                    subAccountVO.setDealerCount((int) subList.stream().filter(it -> it.getRoleIdList().contains(RoleEnum.CompanyRole.DEALER.getCode().toString())).count());

                    ChannelQuery channelQuery = new ChannelQuery();
                    channelQuery.setInvitedId(subAccountVO.getId());
                    channelQuery.resetOnlyPage();
//                    channelDAO.listByQuery(channelQuery);
//                    subAccountVO.setChannelCount((int) channelPage.getTotal());

                    SupplierQuery supplierQuery = new SupplierQuery();
                    supplierQuery.setInviteId(subAccountVO.getId());
                    supplierQuery.resetOnlyPage();
//                    supplierDAO.listByQuery(supplierQuery);
//                    subAccountVO.setSupplierCount((int) supplierPage.getTotal());

                    Object detail = AbsIdentityPolicySupport.getPolicy(role).detail(subAccountVO.getId());
                    if (detail != null) {
                        switch (role) {
                            case OPERATOR:
                                OperatorVO operatorDO = (OperatorVO) detail;
                                TransferUtils.transfer(operatorDO, subAccountVO);
                                subAccountVO.setTotalSupplierAmount(operatorDO.getOrderTotalAmount());
                                Map<String, String> map = JSONUtil.toBean(operatorDO.getInfo(), Map.class);
                                subAccountVO.setHeadImg(MapUtil.getStr(map, "logo"));
                                break;
                            case DEALER:
                                DealerVO dealerDO = (DealerVO) detail;
                                TransferUtils.transfer(dealerDO, subAccountVO);
                                subAccountVO.setTotalSupplierAmount(dealerDO.getOrderTotalAmount());
                                break;
                            case SELECTOR:
                                SelectorVO selectorDO = (SelectorVO) detail;
                                TransferUtils.transfer(selectorDO, subAccountVO);
                                subAccountVO.setTotalSupplierAmount(selectorDO.getOrderTotalAmount());
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
        return resList;
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

    @Override
    public Page<AccountAwardUserVO> awardUserPage(AccountAwardUserQuery query) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setSearch(query.getName());
        accountQuery.setClient(CommonEnum.Client.OPERATOR);

        Page<AccountAwardUserVO> page = accountRepository.pageObj(accountQuery, AccountAwardUserVO.class);
        for (AccountAwardUserVO userVO : page.getRecords()) {
            RoleEnum.CompanyRole companyRole = CollUtil.getFirst(
                    RoleEnumUtil.getLevelUpEnumList(CommonEnum.Client.OPERATOR, userVO.getRoleIdList())
            );
            String roleName = Opt.ofNullable(companyRole).map(RoleEnum.CompanyRole::getValue).orElse("");
            userVO.setRoleName(roleName);
        }
        return page;
    }
}
