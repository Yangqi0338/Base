package com.newzkl.platform.base.biz.account.application.service.impl;


import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountLoginService;
import com.newzkl.platform.base.biz.auth.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.AccountLoginRepository;
import com.newzkl.platform.base.biz.auth.model.assembler.LoginAssembler;
import com.newzkl.platform.base.biz.auth.model.oauth.dto.AccountLoginLogDTO;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.auth.model.oauth.req.*;
import com.newzkl.platform.base.biz.auth.model.oauth.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginAccountRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginRes;
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleVO;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.model.req.VerificationCodeReq;
import com.newzkl.platform.base.common.core.redis.utils.SmsMethod;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.AccountRpcVO;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.auth.AuthEnum;
import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        CommonUtil.validate(loginReq);

        String username = loginReq.getUsername();
        AccountEnum.Identity companyRole = loginReq.getIdentity();
        AuthEnum.Type type = loginReq.getType();
        log.info("开始密码登录流程，查询用户名|手机号：{}", username);

        // 登录与注册为两个独立端口, 登录不存在直接抛 NO_EXIST, 不回退注册(注册有事务, 登录无)
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
                SmsMethod.verificationCode(codeReq);
                break;
            default:
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "不支持的登录类型");
        }

        LoginRes loginRes = login(account, type, companyRole);
        // 权限缓存改为拉取模型(@FuncPermission + AccountPermissionStpInterface), 登录不再预热, 移除 authRepository.cacheUserUnFunctionUrls
        log.info("密码登录成功：账号{}，角色{}，客户端{}", account.getId(), companyRole.getCode(), loginReq.getClient().getCode());
        return loginRes;
    }

    private @NotNull AccountRpcVO findAccount(LoginReq loginReq) {
        // 通常必须传入username和client，不可能会有多条账号数据
        AccountRpcVO targetAccount = accountApi.account(loginReq.getClient(), loginReq.getUsername());

        // 若无可用账号
        if (targetAccount == null) {
            log.error("登录失败：不存在有效账号");
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        // 封禁校验(登录/注册一致)
        assertNotBlocked(targetAccount, "登录");
        // 已注销账号登录自动恢复为正常(login无事务, 失败下次再登录即可)
        if (targetAccount.getState() == AccountEnum.State.DESTROY) {
            log.info("{}对应的账号{}为已注销状态，登录自动恢复", loginReq.getUsername(), targetAccount.getId());
            targetAccount.setState(AccountEnum.State.ENABLE);
            targetAccount.setCancelTime(null);
//            accountApi.accountEdit(targetAccount, null);
        }

        return targetAccount;
    }

    /**
     * 校验账号未被平台封禁, 封禁则抛 {@link AccountErrorCode#IN_BLOCKLIST}
     *
     * <p>登录与注册对 DESTROY/ENABLE 的期望相反(登录要账号存在、注册要账号不存在),
     * 二者状态判断中仅封禁校验语义完全一致, 故只抽取该部分为共享方法</p>
     *
     * @param account 账号
     * @param scene   场景描述(登录/注册), 作为异常补充信息
     */
    private void assertNotBlocked(AccountRpcVO account, String scene) {
        if (account.getState() == AccountEnum.State.DISABLE) {
            log.error("{}失败：账号{}已被平台封禁", scene, account.getId());
            throw new PlatformException(AccountErrorCode.IN_BLOCKLIST, scene);
        }
    }

    @Override
    public LoginRes refreshToken(Long accountId) {
        //查询用户信息 list
        AccountRpcVO account = accountApi.account(null, accountId);
        AccountEnum.Identity identity = findRole(null, account);
        if (account == null || identity == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        //登录
        return login(account, AuthEnum.Type.REFRESH, identity);
    }

    @Override
    public LoginRes toggleClient(ToggleClientReq toggleClientReq) {
        CommonUtil.validate(toggleClientReq);

        Long accountId = toggleClientReq.getAccountId();
        AccountEnum.Client client = toggleClientReq.getClient();

        AccountRpcVO account = accountApi.account(client, accountId);

        //登录
        return login(account, AuthEnum.Type.TOGGLE, null);
    }

    /**
     * 根据指定的角色找可用角色, 若没有指定, 则获取最早的角色
     */
    public AccountEnum.Identity findRole(AccountEnum.Identity companyRole, AccountRpcVO account) {
        if (account == null) return null;
        List<String> roleIdList = StrUtil.split(account.getIdentityList(), ",");
        if (companyRole != null) {
            return roleIdList.contains(companyRole.getCodeStr()) ? companyRole : null;
        } else {
            // 获取最早的角色
            String earlyRoleId = CollUtil.getFirst(roleIdList);
            return AccountEnum.Identity.getByCode(NumberUtil.parseLong(earlyRoleId));
        }
    }

    public LoginRes login(AccountRpcVO account, AuthEnum.Type loginType, AccountEnum.Identity identity) {
        LoginRes loginRes = new LoginRes();
        AccountEnum.Client client = identity.getClient();
        //生成token
        StpUtil.login(account.getId() + client.getCode(), SaLoginConfig
                .setExtra(TokenConstants.DETAILS_CLIENT, client.getCode())
                .setExtra(TokenConstants.DETAILS_ACCOUNT_ID, account.getId())
                .setExtra(TokenConstants.DETAILS_USERNAME, account.getUsername())
                .setExtra(TokenConstants.DETAILS_NICKNAME, account.getNickname())
                .setExtra(TokenConstants.DETAILS_UP_ID, account.getInviteAccountId())
                .setExtra(TokenConstants.DETAILS_IDENTITY, account.getInviteAccountId())
                .setExtra(TokenConstants.DETAILS_ROLE, account.getInviteAccountId())
                .setExtra(TokenConstants.DETAILS_FUNC, account.getInviteAccountId())
        );

        loginRes.setClient(client);
        loginRes.setIdentity(identity);
        loginRes.setToken(StpUtil.getTokenValue());
        LoginAccountRes accountVO = loginAssembler.vo2LoginRes(account);
        loginRes.setAccountVO(accountVO);
        // mq
        loginNotify(account, loginType);
        return loginRes;
    }

    @Async
    public void loginNotify(AccountRpcVO account, AuthEnum.Type loginType) {
        //修改上次登录时间
        Long accountId = account.getId();
        //记录登录日志
        if (loginType.isNeedLog() && accountApi.updateLoginTime(accountId)) {
            AccountLoginLogDTO accountLoginLog = new AccountLoginLogDTO();
            accountLoginLog.init(loginType);
            accountLoginLog.setAccountId(accountId);
            accountLoginRepository.accountLoginLogSave(accountLoginLog);
        }
    }

    @Override
    public Page<AccountLoginLogRes> accountLoginLogPage(AccountLoginLogQuery accountLoginLogQuery) {
        return TransferUtils.transferPage(accountLoginRepository.selectPage(accountLoginLogQuery), loginAssembler::loginLogVO2Res);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginRes register(AccountEnum.Client client, IdentityCustomSaveReq customSaveReq) {
        // 1. 先执行注册(策略按角色分发, identity-spi 迁入后回填)
        AccountRpcVO account = doRegister(client, customSaveReq);

        // 仅注册
        if (Boolean.FALSE.equals(customSaveReq.getLogin())) {
            LoginRes loginRes = new LoginRes();
            loginRes.setIdentity(customSaveReq.getIdentity());
            LoginAccountRes accountVO = new LoginAccountRes();
            accountVO.setId(account.getId());
            accountVO.setUsername(customSaveReq.getUsername());
            loginRes.setAccountVO(accountVO);
            return loginRes;
        }

        // 注册后登录,回登录态
        return login(account, customSaveReq.getType(), customSaveReq.getIdentity());
    }

    /**
     * 执行注册
     *
     * @param req 注册请求
     * @return 新账号 ID
     */
    private AccountRpcVO doRegister(AccountEnum.Client client, IdentityCustomSaveReq req) {
        if (StrUtil.isBlank(req.getUsername()) && StrUtil.isBlank(req.getPhone())) {
            throw new PlatformException(AccountErrorCode.REGISTER_CERTIFICATE_ERROR, "注册");
        }
        AccountEnum.Identity identity = req.getIdentity();
        if (identity == null) {
            identity = client.getDefaultIdentity();
            req.setIdentity(identity);
        }

        // 处理username和phone的数据同步关系
        String username = StrUtil.blankToDefault(req.getUsername(), req.getPhone());
        req.setUsername(username);
        if (PhoneUtil.isPhone(username) && StrUtil.isBlank(req.getPhone())) {
            req.setPhone(username);
        }

        AccountRpcVO existAccount = accountApi.account(client, username);

        IdentityRegisterRpcReq registerRpcReq = TransferUtils.transfer(req, IdentityRegisterRpcReq.class);
        // 是否首次注册: 无同名账号即首次; DESTROY(注销未回收)复用原 id 覆盖注册非首次
        boolean isRegisterOnce = true;
        if (existAccount != null) {
            // 封禁校验(登录/注册一致), 封禁不可复用
            assertNotBlocked(existAccount, "注册");
            if (existAccount.getState() == AccountEnum.State.ENABLE) {
                throw new PlatformException(AccountErrorCode.EXIST_USERNAME, "注册");
            }
            registerRpcReq.setId(existAccount.getId());
            registerRpcReq.setPid(existAccount.getId());
            isRegisterOnce = false;
        }

        registerRpcReq.setRegisterOnce(isRegisterOnce);

        // 短信验证
        AuthEnum.Type type = req.getType();
        switch (type) {
            case PASSWORD:
                break;
            case CODE:
                VerificationCodeReq codeReq = new VerificationCodeReq();
                codeReq.setPhone(req.getPhone());
                codeReq.setType(SmsEnum.Type.Register);
                codeReq.setCode(req.getCode());
                SmsMethod.verificationCode(codeReq);
                break;
            default:
                throw new PlatformException(AccountErrorCode.CERTIFICATE_MISSING, "注册");
        }

        // 有邀请码但是没有邀请id，去数据库查
        String yqm = req.getYqm();
        if (StrUtil.isNotBlank(yqm) && req.getInviteId() == null) {
            AccountRpcVO inviteAccount = accountApi.account(yqm);
            // 邀请人为空或为自己
            if (inviteAccount == null || inviteAccount.getId().equals(registerRpcReq.getId())) {
                throw new PlatformException(AccountErrorCode.PARAM_YQM);
            }
            // 若已经注册且有邀请人，不能再被邀请
            if (!isRegisterOnce && existAccount.getInviteAccountId() != null) {
                throw new PlatformException(AccountErrorCode.PARAM_YQM);
            }
            Long inviteAccountId = inviteAccount.getId();
            log.info("邀请人信息：{}", JSONUtil.toJsonStr(inviteAccount));
            registerRpcReq.setInviteAccountId(inviteAccountId);
            // 邀请人与自己同客户端，同客户端
            if (registerRpcReq.getPid() == null && inviteAccount.getClient() == client) {
                registerRpcReq.setPid(inviteAccountId);
                registerRpcReq.setPidList(BizUtil.getPidList(inviteAccount.getPidList(), inviteAccountId));
                registerRpcReq.setPIdentityList(BizUtil.getPIdentityList(inviteAccount.getPIdentityList(), inviteAccount.getIdentityList()));
            }
        }

        List<IdentityRegisterRpcReq> registerRpcReqList = CollUtil.newArrayList(registerRpcReq);
        if (isRegisterOnce) {
            // TODO 已有供应商角色则不能注册其他角色
            if (Objects.requireNonNull(identity) == AccountEnum.Identity.CHANNEL) {
                // 同步注册member
                // 检查是否已经注册
                CommonEnum.YesOrNo createMember = req.getCreateMember();
                if (createMember == CommonEnum.YesOrNo.YES) {
                    AccountEnum.Client memberClient = AccountEnum.Client.USER;
                    AccountRpcVO memberExistAccount = accountApi.account(memberClient, username);
                    if (memberExistAccount == null) {
                        IdentityRegisterRpcReq memberRegisterRpcReq = TransferUtils.transfer(registerRpcReq, IdentityRegisterRpcReq.class);
                        memberRegisterRpcReq.setClient(memberClient);
                        memberRegisterRpcReq.setIdentity(memberClient.getDefaultIdentity());
                        memberRegisterRpcReq.setRegisterOnce(true);
                        registerRpcReqList.add(memberRegisterRpcReq);
                    }
                }
            }
        }

        // 账号注册
        return accountApi.register(registerRpcReqList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPassword(CodeUpdatePasswordReq req) {
        CommonUtil.validate(req);

        AccountEnum.Client client = req.getClient();
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
        CommonUtil.validate(req);

        //查询用户信息
        Long accountId = req.getAccountId();
        AccountEnum.Client client = req.getClient();
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
    public List<RoleVO> accountRoleList(Long accountId, AccountEnum.Client client) {
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
