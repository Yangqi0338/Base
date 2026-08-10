package com.newzkl.platform.base.biz.account.application.auth.service.impl;

import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.auth.service.AccountLoginService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreAccountCreateReq;
import com.newzkl.platform.base.biz.account.domain.auth.repository.AccountLoginRepository;
import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicySupport;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.assembler.LoginAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.*;
import com.newzkl.platform.base.biz.account.model.auth.res.LoginRes;
import com.newzkl.platform.base.biz.account.model.auth.res.TokenAndExpireRes;
import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountLoginLog;
import com.newzkl.platform.base.biz.account.model.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.account.model.res.LoginAccountRes;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;
import com.newzkl.platform.base.biz.account.model.support.VerificationCodeReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
import com.newzkl.platform.base.biz.account.model.vo.ResetMemberVO;
import com.newzkl.platform.base.biz.account.model.vo.tencent.TLSSigAPIv2;
import com.newzkl.platform.base.biz.account.model.vo.tencent.TencentImConfig;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.lock.impl.RedissonLockUtil;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.redis.utils.ResetPwdRedisUtil;
import com.newzkl.platform.base.common.core.sms.enums.SmsEnum;
import com.newzkl.platform.base.common.core.utils.common.JsonEncryptDecryptUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:47
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountLoginServiceImpl implements AccountLoginService {

    /**
     * 找回密码 nonce 分布式锁 key 前缀 (逐字沿用旧实现)
     */
    private static final String RESET_PWD_NONCE_LOCK_PREFIX = "reset:pwd:nonce:lock:";

    /**
     * 找回密码「设备 + 手机号」验证通过标记 key 前缀 (逐字沿用旧实现)
     */
    private static final String RESET_PWD_DEVICE_PREFIX = "reset:pwd:device:";

    /**
     * 新密码强度: 至少含一个字母与一个数字 (正则逐字沿用旧实现)
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).+$");

    private final UserQueryService userQueryService;
    private final AccountRepository accountRepository;
    private final AccountDomain accountDomain;
    private final AccountLoginRepository accountLoginRepository;
    private final LoginAssembler loginAssembler;

    private final GoodsStoreApi goodsStoreApi;
    private final DictApi dictApi;

    @Override
    public LoginRes accountLogin(LoginReq loginReq) {
        // 参数校验
        BizUtil.validate(loginReq);

        String username = loginReq.getUsername();
        RoleEnum.CompanyRole companyRole = loginReq.getRole();
        AccountEnum.LoginType type = loginReq.getType();
        log.info("开始密码登录流程，查询用户名|手机号：{}", username);

        AccountVO account = findAccount(loginReq);

        companyRole = findRole(companyRole, account);
        if (companyRole == null) {
            log.error("密码登录失败：账号{}无有效角色", account.getId());
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }

        // 因素认证
        switch (type) {
            case PASSWORD:
                boolean checkPassword = account.checkPassword(loginReq.getPassword());
                if (!checkPassword) {
                    throw new PlatformException(AccountErrorCode.PASSWORD);
                }
                break;
            case CODE:
                VerificationCodeReq codeReq = new VerificationCodeReq();
                codeReq.setPhone(account.getPhone());
                codeReq.setType(SmsEnum.Type.Login);
                codeReq.setCode(loginReq.getCode());
                accountRepository.verificationCode(codeReq);
                break;
            default:
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "不支持的登录类型");
        }

        LoginRes loginRes = login(account, AccountEnum.LoginType.PASSWORD, companyRole);

        // 腾讯IM
        generateUserSig(loginRes);
        // 权限缓存改为拉取模型(@FuncPermission + AccountPermissionStpInterface), 登录不再预热, 移除 authRepository.cacheUserUnFunctionUrls
        log.info("密码登录成功：账号{}，角色{}，客户端{}", account.getId(), companyRole.getCode(), loginReq.getClient().getCode());
        return loginRes;
    }

    private @NotNull AccountVO findAccount(LoginReq loginReq) {
        AccountQuery accountQuery = loginAssembler.passwordLoginReq2Query(loginReq);

        // 通常必须传入username和client，不可能会有多条账号数据
        AccountVO targetAccount = accountRepository.account(accountQuery);

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
        } else if (targetAccount.getState() == AccountEnum.State.DISABLE) {
            // 停用状态自动启用
            log.info("{}对应的账号{}为停用状态，自动启用", accountQuery.getCredential(), targetAccount.getId());
            // login不做事务, 失败下次再登录即可
            targetAccount.setState(AccountEnum.State.ENABLE);
            accountRepository.accountEdit(targetAccount, null);
        } else if (targetAccount.getState() == AccountEnum.State.DESTROY) {
            // 禁用状态报错
            log.error("登录失败：{}对应的账号{}已被平台禁用", accountQuery.getCredential(), targetAccount.getId());
            throw new PlatformException(AccountErrorCode.IN_BLOCKLIST, "登录");
        }

        return targetAccount;
    }

    @Override
    public LoginRes subPasswordLogin(String mainUsername, LoginReq loginReq) {
        // 查询主账号
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setMainAccountId(AccountEnum.MAIN_ACCOUNT_PID);
        accountQuery.setUsername(mainUsername);
        accountQuery.setState(AccountEnum.State.ENABLE);
        Long mainAccountId = accountRepository.findId(accountQuery);
        if (mainAccountId == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        loginReq.setMainAccountId(mainAccountId);
        return accountLogin(loginReq);
    }

    @Override
    public LoginRes refreshToken(Long accountId) {
        //查询用户信息
        AccountVO account = accountRepository.account(null, accountId);
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

        AccountVO account = accountRepository.account(null, accountId);
        RoleEnum.CompanyRole companyRole = CollUtil.getLast(RoleEnumUtil.getLevelUpEnumList(client, account.getRoleIdList()));
        if (companyRole == null) {
            throw new PlatformException(AccountErrorCode.NOT_AVAIL_ROLE);
        }

        //登录
        return login(account, AccountEnum.LoginType.TOGGLE, companyRole);
    }

    /**
     * 根据指定的角色找可用角色, 若没有指定, 则获取最早的角色
     */
    public RoleEnum.CompanyRole findRole(RoleEnum.CompanyRole companyRole, AccountVO account) {
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

    public LoginRes login(AccountVO account, AccountEnum.LoginType loginType, RoleEnum.CompanyRole role) {
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
        loginRes.setAccountType(Opt.ofNullable(account.getSubUserType()).orElse(AccountEnum.SubUserType.MAIN));
        loginRes.setEmpType(AuthEnum.EmpType.getByRole(role));
        LoginAccountRes accountVO = loginAssembler.vo2LoginRes(account);
        loginRes.setAccountVO(accountVO);
        // 通知
        BizUtil.async(this, (service) -> service.loginNotify(account, loginType));
        return loginRes;
    }

    @Async
    public void loginNotify(AccountVO account, AccountEnum.LoginType loginType) {
        //修改上次登录时间
        updateLoginTime(account);
        //记录登录日志
        if (loginType.isNeedLog()) {
            AccountLoginLog accountLoginLog = new AccountLoginLog();
            accountLoginLog.init(account, loginType);
            accountLoginRepository.accountLoginLogSave(accountLoginLog);
        }
    }

    public void updateLoginTime(AccountVO account) {
        log.info("【登录时间更新】开始更新账号{}的登录时间", account.getId());
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setId(account.getId());
        if (account.getMainAccountId() != null && account.getMainAccountId() != 0L) {
            accountQuery.add(accountQuery.getIdList(), account.getMainAccountId());
        }
        AccountVO accountUpdate = new AccountVO();
        accountUpdate.setLastLoginTime(LocalDateTime.now());
        accountRepository.accountEdit(accountUpdate, accountQuery);
    }

    public void generateUserSig(LoginRes login) {
        String userAccount = login.getAccountVO().getUserAccount();
        log.info("【UserSig生成】开始为用户{}生成签名", userAccount);
        try {
            TencentImConfig config = dictApi.getTencentImConfig();
            TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(config.getSdkAppId(), config.getSecretKey());
            String userSig = tlsSigAPIv2.genUserSig(config.getIdentifier(), config.getExpire());
            login.setSdkAppId(config.getSdkAppId());
            login.setIdentifier(config.getIdentifier());
            login.setUserSign(userSig);
            log.info("【UserSig生成】用户{}签名生成成功，有效期：{}秒", userAccount, config.getExpire());
        } catch (Exception e) {
            log.error("【UserSig生成】用户{}签名生成失败，原因：{}", userAccount, e.getMessage(), e);
            throw new RuntimeException("生成UserSig失败：" + e.getMessage(), e);
        }
    }

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
     * 通过账号ID获取Token及剩余有效期
     *
     * @param accountId 账号ID（即account.getId()）
     * @param client
     * @return TokenAndExpireVO（无有效登录时抛出异常）
     */
    @Override
    public TokenAndExpireRes getTokenAndExpireById(Long accountId, CommonEnum.Client client) {
        // 1. 参数校验（对齐原有代码的异常风格）
        if (accountId == null) {
            log.error("获取Token失败：账号ID不能为空");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "账号ID不能为空");
        }
        String key = accountId + client.getCode();
        // 2. 检查账号是否已登录
        if (!StpUtil.isLogin(key)) {
            log.error("获取Token失败：账号{}未登录", accountId);
            throw new PlatformException(AccountErrorCode.NO_EXIST, "该账号未登录，无有效Token");
        }

        // 3. 获取该账号的所有登录Token（兼容多端登录场景）
        // 若只需最新登录的Token，用 getTokenValueByLoginId；若需所有，用 getTokenValueListByLoginId
        String latestToken = StpUtil.getTokenValueByLoginId(key);
        if (StringUtils.isBlank(latestToken)) {
            log.error("获取Token失败：账号{}无有效登录Token", accountId);
            throw new PlatformException(AccountErrorCode.NO_EXIST, "该账号无有效登录Token");
        }

        // 4. 获取Token对应的设备类型（可选，按需添加）
        String loginDevice = null;
        List<TokenSign> tokenSignList = StpUtil.getTokenSignListByLoginId(key, null);
        if (CollUtil.isNotEmpty(tokenSignList)) {
            // 取最新登录的Token的设备类型
            TokenSign latestTokenSign = CollUtil.getLast(tokenSignList);
            loginDevice = latestTokenSign.getDevice();
        }

        // 5. 获取Token剩余有效期（单位：秒）
        // Sa-Token API说明：
        // - 返回 -1 = 永久有效
        // - 返回 -2 = Token不存在/已失效
        long expireSeconds = StpUtil.getTokenTimeout(latestToken);

        // 6. 封装返回结果
        TokenAndExpireRes result = new TokenAndExpireRes();
        result.setAccountId(accountId);
        result.setToken(latestToken);
        result.setExpireSeconds(expireSeconds);
        result.setDevice(loginDevice);

        log.info("获取账号{}的Token及有效期成功：token={}, 剩余有效期={}秒, 设备类型={}",
                accountId, latestToken, expireSeconds, loginDevice);
        return result;
    }

    @Override
    // TODO 看看能不能cover掉全局事务
    @Transactional(rollbackFor = Exception.class)
    public void accountBatchRegister(List<CustomSaveBatchReq> customSaveBatchReqList) {
        for (CustomSaveBatchReq customSaveBatchReq : customSaveBatchReqList) {
            IdentityCustomSaveReq customSaveReq = TransferUtils.transfer(customSaveBatchReq, IdentityCustomSaveReq::new);
            for (Long roleId : customSaveBatchReq.getRoleIdList()) {
                customSaveReq.setRole(RoleEnum.CompanyRole.getByCode(roleId));
                AbsIdentityPolicySupport.getPolicy(roleId).customRegister(customSaveReq);
            }
        }
    }

    @Override
    public Long customeRegister(IdentityCustomSaveReq customSaveReq) {
        // 手机号去重校验
        IdentityRegisterRes identityRegisterRes =
                AbsIdentityPolicySupport.getPolicy(customSaveReq.getRole()).customRegister(customSaveReq);
        if (!identityRegisterRes.isSuccess()) {
            throw new PlatformException(identityRegisterRes.getErrorCode());
        }
        return identityRegisterRes.getId();
    }

    /**
     * 处理会员账号不存在时的自动注册逻辑
     *
     * @param codeLoginReq 登录请求参数
     * @return 注册后登录的结果
     */
    public boolean handleMemberAutoRegister(CodeLoginRegisterReq codeLoginReq) {
        //  构建注册参数并执行会员注册
        IdentityCustomSaveReq memberRegisterReq = buildMemberRegisterReqAndRegister(codeLoginReq);
        IdentityRegisterRes registerRes = AbsIdentityPolicySupport.getPolicy(memberRegisterReq.getRole())
                .customRegister(memberRegisterReq);

        // 注册失败则抛出异常，成功则重新执行登录
        if (registerRes.getErrorCode() != null) {
            throw new PlatformException(registerRes.getErrorCode());
        }

        // 新增门店用户关系
        StoreAccountCreateReq storeAccountReq = createStoreAccountRelation(memberRegisterReq.getYqm(), registerRes.getId());
        goodsStoreApi.createStoreAccount(storeAccountReq);
        return true;
    }

    /**
     * 构建会员注册请求
     *
     * @param codeLoginReq 登录请求参数
     * @return 构建体
     */
    private IdentityCustomSaveReq buildMemberRegisterReqAndRegister(CodeLoginRegisterReq codeLoginReq) {
        IdentityCustomSaveReq memberRegisterReq = loginAssembler.codeLoginRegisterReq2SaveReq(codeLoginReq);
        if (StrUtil.isNotBlank(codeLoginReq.getSuperiorAccount())) {
            AccountQuery accountQuery = new AccountQuery();
            accountQuery.setUserAccount(codeLoginReq.getSuperiorAccount());
            AccountVO account = accountRepository.account(accountQuery);
            if (Objects.isNull(account)) {
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "上级账号错误，不存在");
            }
            memberRegisterReq.setYqm(account.getYqm());
        }
        return memberRegisterReq;
    }

    /**
     * 新增门店用户关系
     */
    private StoreAccountCreateReq createStoreAccountRelation(String yqm, Long accountId) {
        StoreAccountCreateReq storeAccountCreateReq = new StoreAccountCreateReq();
        storeAccountCreateReq.setAccountId(accountId);
        storeAccountCreateReq.setDefult(CommonEnum.YesOrNo.YES.getCode());

        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setYqm(yqm);
        AccountVO inviteAccountVO = accountRepository.account(accountQuery);
        if (inviteAccountVO != null) {
            storeAccountCreateReq.setStoreId(inviteAccountVO.getId());
        } else {
            // TODO 临时写死默认平台门店，后续需要根据实际需求进行处理
            storeAccountCreateReq.setStoreId(753260854440005L);
        }
        return storeAccountCreateReq;
    }

    @Override
    public Long inviteSupplierRegister(IdentityCustomSaveReq customSaveReq, String host) {
        // 通过host查找对应的运营商
        OperatorVO operatorByDomain = userQueryService.getOperatorByDomain(host);
        if (operatorByDomain == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "域名错误，运营商不存在");
        }
        customSaveReq.setYqm(operatorByDomain.getYqm());
        IdentityRegisterRes accountRegisterRes = AbsIdentityPolicySupport.getPolicy(RoleEnum.CompanyRole.SUPPLIER)
                .customRegister(customSaveReq);

        if (accountRegisterRes.getErrorCode() != null) {
            throw new PlatformException(accountRegisterRes.getErrorCode());
        }
        return accountRegisterRes.getId();
    }

    @Override
    public void resetPasswordSmsCode(ResetMemberReq req) {
        log.info("开始执行重置用户密码-验证手机号");
        // 1. 解密 + 基础参数校验
        ResetMemberVO resetMemberVO = decryptAndValidateBaseParam(req);

        // 2. 验证码步骤专属参数校验 (保留旧 isAllBlank 语义: 四项全空才拦)
        if (StringUtils.isAllBlank(resetMemberVO.getPhone(), resetMemberVO.getCode(),
                resetMemberVO.getDeviceCode(), resetMemberVO.getNonce())) {
            log.error("重置密码失败：手机号、验证码、设备码、nonce必填");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "手机号、验证码、设备码、nonce必填");
        }

        // 3. 限流
        if (ResetPwdRedisUtil.isOverRateLimit(resetMemberVO.getPhone(), 1, 60)) {
            log.error("重置密码失败：请求过于频繁，请1分钟后重试");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "请求过于频繁，请1分钟后重试");
        }

        // 4. 防重放
        String nonceLockKey = RESET_PWD_NONCE_LOCK_PREFIX + resetMemberVO.getNonce();
        try {
            if (!RedissonLockUtil.tryLock(nonceLockKey, TimeUnit.SECONDS, 3, 5)) {
                log.error("重置密码失败：获取nonce锁失败");
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "系统繁忙，请稍后再试");
            }
            if (ResetPwdRedisUtil.isNonceUsed(resetMemberVO.getNonce())) {
                log.error("重置密码失败：nonce失效，请重新获取");
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "nonce失效，请重新获取");
            }

            // 5. 查询有效用户
            validAccountByPhone(resetMemberVO.getPhone());

            // 6. 校验验证码 (校验不过由仓储抛 CODE_ERROR)
            VerificationCodeReq codeReq = new VerificationCodeReq();
            codeReq.setPhone(resetMemberVO.getPhone());
            codeReq.setCode(resetMemberVO.getCode());
            codeReq.setType(SmsEnum.Type.UpdatePassword);
            accountRepository.verificationCode(codeReq);

            // 7. 标记 nonce 已使用
            ResetPwdRedisUtil.markNonceUsed(resetMemberVO.getNonce(), ResetPwdRedisUtil.EXPIRE_MINUTES);

            // 8. 缓存验证通过标记
            RedisUtil.set(devicePhoneKey(resetMemberVO.getPhone(), resetMemberVO.getDeviceCode()), Boolean.TRUE,
                    ResetPwdRedisUtil.EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("重置密码-验证手机号成功");
        } finally {
            RedissonLockUtil.unlock(nonceLockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetMemberPasswordUpdate(ResetMemberReq req) {
        log.info("开始执行重置用户密码-更新密码");
        // 1. 解密 + 基础参数校验
        ResetMemberVO resetMemberVO = decryptAndValidateBaseParam(req);

        // 2. 更新步骤专属参数校验 (保留旧 isAllBlank 语义)
        if (StringUtils.isAllBlank(resetMemberVO.getPhone(), resetMemberVO.getNewPassword(),
                resetMemberVO.getDeviceCode(), resetMemberVO.getNonce())) {
            log.error("重置密码失败：手机号、新密码、设备码、nonce必填");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "手机号、新密码、设备码、nonce必填");
        }

        // 3. 校验第一步留下的验证标记
        String devicePhoneKey = devicePhoneKey(resetMemberVO.getPhone(), resetMemberVO.getDeviceCode());
        if (!RedisUtil.exists(devicePhoneKey) || Boolean.FALSE.equals(RedisUtil.get(devicePhoneKey))) {
            log.error("重置密码失败：验证结果已失效或未通过");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "验证结果已失效或未通过");
        }

        // 4. 查询有效用户
        AccountVO account = validAccountByPhone(resetMemberVO.getPhone());

        // 5. 密码强度校验
        String newPassword = resetMemberVO.getNewPassword();
        if (newPassword == null || newPassword.length() < 8 || !PASSWORD_PATTERN.matcher(newPassword).matches()) {
            log.error("重置密码失败：密码强度不足");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "密码需至少8位，包含字母和数字");
        }

        // 6. 更新密码
        AccountVO accountEdit = new AccountVO();
        accountEdit.setId(account.getId());
        accountEdit.setPassword(account.getNewPassword(newPassword));
        accountRepository.accountEdit(accountEdit, null);

        // 7. 删除验证标记, 避免重复使用
        RedisUtil.del(devicePhoneKey);
        log.info("重置密码-更新密码成功，accountId: {}", account.getId());
    }

    /**
     * 解密 sign 并做基础校验 (sign 非空 + 时间戳非空且未过期)
     *
     * @param req 找回密码请求
     * @return 解密后的找回密码视图
     */
    private ResetMemberVO decryptAndValidateBaseParam(ResetMemberReq req) {
        if (StrUtil.isBlank(req.getSign())) {
            log.error("重置密码失败：加密参数缺失");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "加密参数缺失");
        }
        ResetMemberVO resetMemberVO;
        try {
            resetMemberVO = JsonEncryptDecryptUtils.decryptToObject(req.getSign(), ResetMemberVO.class);
        } catch (IllegalArgumentException e) {
            log.error("重置密码失败：sign解密失败, {}", e.getMessage());
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "加密参数无效或已过期");
        }
        if (resetMemberVO.getTimestamp() == null) {
            log.error("重置密码失败：时间戳必填");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "时间戳必填");
        }
        if (JsonEncryptDecryptUtils.isExpired(resetMemberVO.getTimestamp())) {
            log.error("重置密码失败：请求已过期");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "请求已过期，请重新提交");
        }
        return resetMemberVO;
    }

    /**
     * 按手机号查有效主账号
     *
     * <p>等价旧 {@code accountIdByPhone(MAIN_ACCOUNT_PID, phone, ENABLE)} + {@code account(accountId)},
     * 旧实现同样不限端与角色</p>
     *
     * @param phone 手机号
     * @return 账号视图
     */
    private AccountVO validAccountByPhone(String phone) {
        AccountQuery query = new AccountQuery();
        query.setMainAccountId(AccountEnum.MAIN_ACCOUNT_PID);
        query.setPhone(phone);
        query.setState(AccountEnum.State.ENABLE);
        Long accountId = accountRepository.findId(query);
        if (accountId == null) {
            log.error("重置密码失败：用户不存在");
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "用户不存在");
        }
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setId(accountId);
        AccountVO account = accountRepository.account(accountQuery);
        if (Objects.isNull(account)) {
            log.error("重置密码失败：账号不存在，accountId: {}", accountId);
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "账号不存在");
        }
        return account;
    }

    /**
     * 构建「设备 + 手机号」验证通过标记的 Redis Key
     *
     * <p>Key 前缀逐字沿用旧实现, 保证灰度期新旧代码互认</p>
     *
     * @param phone      手机号
     * @param deviceCode 设备码
     * @return Redis Key
     */
    private String devicePhoneKey(String phone, String deviceCode) {
        return RESET_PWD_DEVICE_PREFIX + phone + ":" + deviceCode;
    }

}
