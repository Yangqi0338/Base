package com.newzkl.platform.base.biz.account.application.service.impl;


import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountLoginService;
import com.newzkl.platform.base.biz.auth.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.AccountLoginRepository;
import com.newzkl.platform.base.biz.auth.model.assembler.LoginAssembler;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.auth.model.oauth.req.*;
import com.newzkl.platform.base.biz.auth.model.oauth.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginAccountRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginRes;
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleVO;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:47
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountLoginServiceImpl implements AccountLoginService {

    private final AccountLoginRepository accountLoginRepository;
    private final LoginAssembler loginAssembler;
    private final AccountApi accountApi;

    @Override
    public LoginRes accountLogin(LoginReq loginReq) {
        // 参数校验
        BizUtil.validate(loginReq);

        String username = loginReq.getUsername();
        RoleEnum.CompanyRole companyRole = loginReq.getRole();
        AccountEnum.LoginType type = loginReq.getType();
        log.info("开始密码登录流程，查询用户名|手机号：{}", username);

        AccountRpcVO account = findAccount(loginReq);

        companyRole = findRole(companyRole, account);
        if (companyRole == null) {
            log.error("密码登录失败：账号{}无有效角色", account.getId());
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }

        // 因素认证
        switch (type) {
            case PASSWORD:
                boolean checkPassword = SecurityUtils.matchesPassword(loginReq.getPassword(), account.getPassword());
                if (!checkPassword) {
                    throw new PlatformException(AccountErrorCode.PASSWORD);
                }
                break;
            case CODE:
                VerificationCodeReq codeReq = new VerificationCodeReq();
                codeReq.setPhone(account.getPhone());
                codeReq.setType(SmsEnum.Type.Login);
                codeReq.setCode(loginReq.getCode());
                verificationCode(codeReq);
                break;
            default:
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "不支持的登录类型");
        }

        LoginRes loginRes = login(account, AccountEnum.LoginType.PASSWORD, companyRole);
        // 权限缓存改为拉取模型(@FuncPermission + AccountPermissionStpInterface), 登录不再预热, 移除 authRepository.cacheUserUnFunctionUrls
        log.info("密码登录成功：账号{}，角色{}，客户端{}", account.getId(), companyRole.getCode(), loginReq.getClient().getCode());
        return loginRes;
    }

    private void verificationCode(VerificationCodeReq codeReq) {

    }

    public String getNewPassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    private @NotNull AccountRpcVO findAccount(LoginReq loginReq) {
        // 通常必须传入username和client，不可能会有多条账号数据
        AccountRpcVO targetAccount = accountApi.account(loginReq);

        // 移除掉用户侧注销且超过24的账号 (不在SQL做是因为效率问题)
        if (targetAccount != null && targetAccount.getState() == AccountEnum.State.DISABLE && targetAccount.getClient() == CommonEnum.Client.USER) {
            if (targetAccount.getCancelTime() == null || LocalDateTime.now().minusDays(1).isAfter(targetAccount.getCancelTime())) {
                targetAccount = null;
            }
        }

        // 若无可用账号
        if (targetAccount == null) {
            log.error("登录失败：不存在有效账号");
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
//        else if (targetAccount.getState() == AccountEnum.State.DISABLE) {
//            // 停用状态自动启用
//            log.info("{}对应的账号{}为停用状态，自动启用", accountQuery.getCredential(), targetAccount.getId());
//            // login不做事务, 失败下次再登录即可
//            targetAccount.setState(AccountEnum.State.ENABLE);
//            accountEdit(targetAccount, null);
//        }
        else if (targetAccount.getState() == AccountEnum.State.DESTROY) {
            // 禁用状态报错
            log.error("登录失败：{}对应的账号{}已被平台禁用", loginReq.getUsername(), targetAccount.getId());
            throw new PlatformException(AccountErrorCode.IN_BLOCKLIST, "登录");
        }

        return targetAccount;
    }

    @Override
    public LoginRes refreshToken(Long accountId) {
        //查询用户信息 list
        AccountRpcVO account = accountApi.account(null, accountId);
        RoleEnum.CompanyRole role = findRole(null, account);
        if (account == null || role == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        //登录
        return login(account, AccountEnum.LoginType.REFRESH, role);
    }

    @Override
    public LoginRes toggleClient(ToggleClientReq toggleClientReq) {
        BizUtil.validate(toggleClientReq);

        Long accountId = toggleClientReq.getAccountId();
        CommonEnum.Client client = toggleClientReq.getClient();

        AccountRpcVO account = accountApi.account(client, accountId);

        //登录
        return login(account, AccountEnum.LoginType.TOGGLE, null);
    }

    /**
     * 根据指定的角色找可用角色, 若没有指定, 则获取最早的角色
     */
    public RoleEnum.CompanyRole findRole(RoleEnum.CompanyRole companyRole, AccountRpcVO account) {
        if (account == null) return null;
        List<String> roleIdList = StrUtil.split(account.getRoleIdList(), ",");
        if (companyRole != null) {
            return roleIdList.contains(companyRole.getCodeStr()) ? companyRole : null;
        } else {
            // 获取最早的角色
            String earlyRoleId = CollUtil.getFirst(roleIdList);
            return RoleEnum.CompanyRole.getByCode(NumberUtil.parseLong(earlyRoleId));
        }
    }

    public LoginRes login(AccountRpcVO account, AccountEnum.LoginType loginType, RoleEnum.CompanyRole role) {
        LoginRes loginRes = new LoginRes();
        CommonEnum.Client client = role.getClient();
        //生成token
        StpUtil.login(account.getId() + client.getCode(), SaLoginConfig
                .setExtra(TokenConstants.DETAILS_CLIENT, client.getCode())
                .setExtra(TokenConstants.DETAILS_ACCOUNT_ID, account.getId())
                .setExtra(TokenConstants.DETAILS_USERNAME, account.getUsername())
                .setExtra(TokenConstants.DETAILS_NICKNAME, account.getNickname())
                .setExtra(TokenConstants.DETAILS_UP_ID, account.getInviteAccountId())
        );

        loginRes.setClient(client);
        loginRes.setRole(role);
        loginRes.setToken(StpUtil.getTokenValue());
        LoginAccountRes accountVO = loginAssembler.vo2LoginRes(account);
        loginRes.setAccountVO(accountVO);
        // 通知 TODO
//        loginNotify(account, loginType);
        return loginRes;
    }

//    @Async
//    public void loginNotify(AccountRpcVO account, AccountEnum.LoginType loginType) {
//        //修改上次登录时间
//        updateLoginTime(account);
//        //记录登录日志
//        if (loginType.isNeedLog()) {
//            AccountLoginLogDTO accountLoginLog = new AccountLoginLogDTO();
//            accountLoginLog.init(loginType);
//            accountLoginLog.setAccountId(acc);
//            accountLoginRepository.accountLoginLogSave(accountLoginLog);
//        }
//    }

//    public void updateLoginTime(AccountVO account) {
//        log.info("【登录时间更新】开始更新账号{}的登录时间", account.getId());
//        AccountQuery accountQuery = new AccountQuery();
//        accountQuery.setId(account.getId());
//        if (account.getMainAccountId() != null && account.getMainAccountId() != 0L) {
//            accountQuery.add(accountQuery.getIdList(), account.getMainAccountId());
//        }
//        AccountVO accountUpdate = new AccountVO();
//        accountUpdate.setLastLoginTime(LocalDateTime.now());
//        accountRepository.accountEdit(accountUpdate, accountQuery);
//    }

    @Override
    public Page<AccountLoginLogRes> accountLoginLogPage(AccountLoginLogQuery accountLoginLogQuery) {
        return TransferUtils.transferPage(accountLoginRepository.selectPage(accountLoginLogQuery), loginAssembler::loginLogVO2Res);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginRes loginRegister(CodeLoginRegisterReq codeLoginRegisterReq) {
        // 1. 优先执行登录逻辑
        LoginReq loginReq = loginAssembler.codeLoginRegisterReq2LoginReq(codeLoginRegisterReq);
        loginReq.setType(AccountEnum.LoginType.CODE);
        try {
            return accountLogin(loginReq);
        } catch (PlatformException e) {
            // 2. C端用户且登录错误为账号不存在, 进行注册
            LoginRes loginRes = null;
            if (codeLoginRegisterReq.getClient() == CommonEnum.Client.USER ||
                    e.equalsCode(AccountErrorCode.NO_EXIST)) {
                // 注册成功调用账号登录
                if (handleMemberAutoRegister(codeLoginRegisterReq)) {
                    loginRes = accountLogin(loginReq);
                }
            }
            // 其他直接抛出异常
            if (loginRes == null) {
                throw e;
            }
            return loginRes;
        }
    }

    /**
     * 处理会员账号不存在时的自动注册逻辑
     *
     * @param codeLoginReq 登录请求参数
     * @return 注册后登录的结果
     */
    public boolean handleMemberAutoRegister(CodeLoginRegisterReq codeLoginReq) {
        //  构建注册参数并执行会员注册
//        IdentityCustomSaveReq memberRegisterReq = buildMemberRegisterReqAndRegister(codeLoginReq);
//        IdentityRegisterRes registerRes = AbsIdentityPolicySupport.getPolicy(memberRegisterReq.getRole())
//                .customRegister(memberRegisterReq);
//
//        // 注册失败则抛出异常，成功则重新执行登录
//        if (registerRes.getErrorCode() != null) {
//            throw new PlatformException(registerRes.getErrorCode());
//        }

        // 新增门店用户关系
//        StoreAccountCreateReq storeAccountReq = createStoreAccountRelation(memberRegisterReq.getYqm(), registerRes.getId());
//        goodsStoreApi.createStoreAccount(storeAccountReq);
        return true;
    }

    /**
     * 构建会员注册请求
     *
     * @param codeLoginReq 登录请求参数
     * @return 构建体
     */
//    private IdentityCustomSaveReq buildMemberRegisterReqAndRegister(CodeLoginRegisterReq codeLoginReq) {
//        IdentityCustomSaveReq memberRegisterReq = loginAssembler.codeLoginRegisterReq2SaveReq(codeLoginReq);
//        if (StrUtil.isNotBlank(codeLoginReq.getSuperiorAccount())) {
//            AccountQuery accountQuery = new AccountQuery();
//            accountQuery.setUserAccount(codeLoginReq.getSuperiorAccount());
//            AccountVO account = accountRepository.account(accountQuery);
//            if (Objects.isNull(account)) {
//                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "上级账号错误，不存在");
//            }
//            memberRegisterReq.setYqm(account.getYqm());
//        }
//        return memberRegisterReq;
//    }

    /**
     * 新增门店用户关系
     */
//    private StoreAccountCreateReq createStoreAccountRelation(String yqm, Long accountId) {
//        StoreAccountCreateReq storeAccountCreateReq = new StoreAccountCreateReq();
//        storeAccountCreateReq.setAccountId(accountId);
//        storeAccountCreateReq.setDefult(CommonEnum.YesOrNo.YES.getCode());
//
//        AccountQuery accountQuery = new AccountQuery();
//        accountQuery.setYqm(yqm);
//        AccountVO inviteAccountVO = accountRepository.account(accountQuery);
//        if (inviteAccountVO != null) {
//            storeAccountCreateReq.setStoreId(inviteAccountVO.getId());
//        } else {
//            // TODO 临时写死默认平台门店，后续需要根据实际需求进行处理
//            storeAccountCreateReq.setStoreId(753260854440005L);
//        }
//        return storeAccountCreateReq;
//    }
    @Override
    public Long customeRegister(IdentityCustomSaveReq customSaveReq) {
//        // 手机号去重校验
//        IdentityRegisterRes identityRegisterRes =
//                AbsIdentityPolicySupport.getPolicy(customSaveReq.getRole()).customRegister(customSaveReq);
//        if (!identityRegisterRes.isSuccess()) {
//            throw new PlatformException(identityRegisterRes.getErrorCode());
//        }
//        return identityRegisterRes.getId();
        return 0L;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPassword(CodeUpdatePasswordReq req) {
        BizUtil.validate(req);

        CommonEnum.Client client = req.getClient();
        Long accountId = req.getAccountId();
        //查询用户信息
//        AccountRpcVO account = accountApi.account(client, accountId);

        //检查验证码
        VerificationCodeReq codeReq = new VerificationCodeReq();
//        codeReq.setPhone(account.getPhone());
        codeReq.setCode(req.getCode());
        codeReq.setType(SmsEnum.Type.UpdatePassword);
//        verificationCode(codeReq);
//        //获取新密码
//        String newPassword = account.getNewPassword(req.getNewPassword());
//        //持久化
//        AccountVO accountEdit = new AccountVO();
//        accountEdit.setId(account.getId());
//        accountEdit.setPassword(newPassword);
//        accountRepository.accountEdit(accountEdit, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUsername(CodeUpdateUsernameReq req) {
        BizUtil.validate(req);

        //查询用户信息
        Long accountId = req.getAccountId();
        CommonEnum.Client client = req.getClient();
        String newUsername = req.getNewUsername();
//        AccountRpcVO account = accountApi.account(client, accountId);

        //校验新账号名称
//        AccountQuery accountQuery = new AccountQuery();
//        accountQuery.setUsername(req.getNewUsername());
//        accountQuery.setClient(client);
//        accountQuery.setState(AccountEnum.State.ENABLE);
//        Long newAccountId = accountRepository.findId(accountQuery);
//        if (newAccountId != null) {
//            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
//        }

        // 若新账号名不是手机号,则使用旧账号的手机号
//        String phone = PhoneUtil.isPhone(newUsername) ? newUsername : account.getPhone();
        //校验验证码
        VerificationCodeReq codeReq = new VerificationCodeReq();
//        codeReq.setPhone(phone);
        codeReq.setCode(req.getCode());
        codeReq.setType(SmsEnum.Type.UpdateUsername);
//        verificationCode(codeReq);

//        //持久化
//        AccountVO accountUpdate = new AccountVO();
//        accountUpdate.setId(account.getId());
//        accountUpdate.setUsername(req.getNewUsername());
//        accountRepository.accountEdit(accountUpdate, null);
    }

    @Override
    public List<RoleVO> accountRoleList(Long accountId, CommonEnum.Client client) {
//        AccountQuery accountQuery = new AccountQuery();
//        accountQuery.setAccountId(accountId);
//        accountQuery.setClient(client);
//        accountQuery.addField(AccountVO::getClient, AccountVO::getRoleIdList);
//
//        List<AccountVO> accountVOList = accountRepository.accountList(accountQuery);

        List<RoleVO> roleList = new ArrayList<>();
//        if (CollUtil.isEmpty(accountVOList)) {
//            return roleList;
//        }

//        // 根据端进行分组
//        accountVOList.stream().collect(CommonUtil.groupingSingleBy(AccountVO::getClient))
//                .forEach((key, accountVO) -> {
//                    List<RoleEnum.CompanyRole> companyRoleList = RoleEnumUtil.getLevelUpEnumList(key, accountVO.getRoleIdList());
//                    RoleVO roleVO = new RoleVO();
//                    roleVO.setRoleList(companyRoleList);
//                    roleVO.setRoleName(companyRoleList.stream().map(RoleEnum.CompanyRole::getCodeStr).collect(Collectors.joining("、")));
//                    roleVO.setClient(key);
//                    roleVO.setShowName(roleVO.getRoleName());
//                    roleList.add(roleVO);
//                });

        return roleList;
    }

}
