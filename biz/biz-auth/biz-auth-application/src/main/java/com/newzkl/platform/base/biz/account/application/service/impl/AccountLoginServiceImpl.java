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
import com.newzkl.platform.base.biz.auth.domain.adapt.api.SupplierApi;
import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.AccountLoginRepository;
import com.newzkl.platform.base.biz.auth.model.assembler.LoginAssembler;
import com.newzkl.platform.base.biz.auth.model.oauth.dto.AccountLoginLogDTO;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.auth.model.oauth.req.*;
import com.newzkl.platform.base.biz.auth.model.oauth.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginAccountRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginRes;
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
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    private final SupplierApi supplierApi;

    @Override
    public LoginRes accountLogin(AccountEnum.Client client, LoginReq loginReq) {
        // 参数校验
        CommonUtil.validate(loginReq);

        String username = loginReq.getUsername();
        AuthEnum.Type type = loginReq.getType();
        log.info("开始登录流程，查询用户名|手机号：{}", username);

        // 登录与注册为两个独立端口, 登录不存在直接抛 NO_EXIST, 不回退注册(注册有事务, 登录无)
        // client 为空(前端未指定端)时同一 username 可能存在多端账号, 全部拉出供按端优先级择主
        List<AccountRpcVO> accountList = findAccountList(client, username);

        // 因素认证 + 择主账号: 密码登录先按密码正确性筛选, 再按 Client.values() 端优先级取第一为主账号
        AccountRpcVO account = authAndPickMain(accountList, loginReq, type);

        AccountEnum.Identity identity = findIdentity(loginReq.getIdentity(), account);
        if (identity == null) {
            log.error("登录失败：账号{}无有效角色", account.getId());
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }

        LoginRes loginRes = login(account, type, identity);
        loginRes.setClient(accountList.stream().map(AccountRpcVO::getClient).collect(Collectors.toList()));
        // 权限缓存改为拉取模型(@FuncPermission + AccountPermissionStpInterface), 登录不再预热, 移除 authRepository.cacheUserUnFunctionUrls
        log.info("登录成功：账号{}，角色{}，客户端{}", account.getId(), identity, account.getClient());
        return loginRes;
    }

    /**
     * 因素认证并择定登录主账号
     *
     * <p>密码登录: 先筛出密码正确的账号, 再按 {@link AccountEnum.Client#values()} 端优先级取第一为主账号;
     * 筛选为空时, 若存在未设密码的账号则提示 {@link AccountErrorCode#PASSWORD_NOT_SET}, 否则为密码错误。
     * 验证码登录: 先按端优先级择主, 再以主账号手机号校验验证码。</p>
     *
     * @param accountList 候选账号列表 (非空)
     * @param loginReq    登录请求
     * @param type        登录因素类型
     * @return 登录主账号
     */
    private AccountRpcVO authAndPickMain(List<AccountRpcVO> accountList, LoginReq loginReq, AuthEnum.Type type) {
        switch (type) {
            case PASSWORD:
                List<AccountRpcVO> matched = accountList.stream()
                        .filter(a -> StrUtil.isNotBlank(a.getPassword())
                                && SecurityUtils.matchesPassword(loginReq.getPassword(), a.getPassword()))
                        .collect(Collectors.toList());
                if (CollUtil.isEmpty(matched)) {
                    boolean anyNoPassword = accountList.stream().anyMatch(a -> StrUtil.isBlank(a.getPassword()));
                    throw new PlatformException(anyNoPassword ? AccountErrorCode.PASSWORD_NOT_SET : AccountErrorCode.PASSWORD);
                }
                return pickByClientOrder(matched);
            case CODE:
                VerificationCodeReq codeReq = new VerificationCodeReq();
                codeReq.setPhone(loginReq.getUsername());
                codeReq.setType(SmsEnum.Type.Login);
                codeReq.setCode(loginReq.getCode());
                SmsMethod.verificationCode(codeReq);
                return pickByClientOrder(accountList);
            default:
                throw new PlatformException(AccountErrorCode.PARAM_ERROR, "不支持的登录类型");
        }
    }

    /**
     * 按 {@link AccountEnum.Client#values()} 端优先级取列表中第一个匹配的账号
     *
     * @param accountList 候选账号列表 (非空)
     * @return 端优先级最高的账号, 无端匹配时兜底返回首个
     */
    private AccountRpcVO pickByClientOrder(List<AccountRpcVO> accountList) {
        for (AccountEnum.Client c : AccountEnum.Client.values()) {
            for (AccountRpcVO account : accountList) {
                if (account.getClient() == c) {
                    return account;
                }
            }
        }
        return CollUtil.getFirst(accountList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginRes loginRegister(AccountEnum.Client client, LoginReq loginReq) {
        try {
            return accountLogin(SecurityUtils.getRequestClient(), loginReq);
        } catch (PlatformException e) {
            // 仅账号不存在时回退注册, 其余异常原样抛出
            if (!e.equalsCode(AccountErrorCode.NO_EXIST)) {
                throw e;
            }
            log.info("登录并注册：账号{}不存在，回退自动注册后登录", loginReq.getUsername());
            return register(client, buildRegisterReq(loginReq));
        }
    }

    /**
     * 由登录入参构造注册入参, 复用登录字段
     *
     * <p>注册后是否自动登录由后端固定为 {@code TRUE}, 不接收前端开关</p>
     *
     * @param loginReq 登录请求
     * @return 注册请求
     */
    private RegisterReq buildRegisterReq(LoginReq loginReq) {
        RegisterReq req = new RegisterReq();
        req.setUsername(loginReq.getUsername());
        req.setPassword(loginReq.getPassword());
        req.setCode(loginReq.getCode());
        req.setIdentity(loginReq.getIdentity());
        // 注册后自动登录由后端写死 TRUE, 不由前端传入
        req.setLogin(Boolean.TRUE);
        return req;
    }

    /**
     * 按凭证查全部候选端账号并逐一做登录态校验
     *
     * <p>client 非空时限定单端(仍走列表口径, 通常至多一条); client 为空时同一 username
     * 可能存在多端账号, 全部返回供上层按端优先级择主。每个账号均做封禁校验, 注销账号自动恢复。</p>
     *
     * @param client   端, 可空
     * @param username 登录凭证 (username | phone)
     * @return 校验后的候选账号列表 (非空)
     */
    private @NotNull List<AccountRpcVO> findAccountList(AccountEnum.Client client, String username) {
        List<AccountRpcVO> accountList = accountApi.accountList(client, username);

        // 若无可用账号
        if (CollUtil.isEmpty(accountList)) {
            log.error("登录失败：不存在有效账号");
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        for (AccountRpcVO account : accountList) {
            // 封禁校验(登录/注册一致)
            assertNotBlocked(account, "登录");
            // 已注销账号登录自动恢复为正常(login无事务, 失败下次再登录即可)
            if (account.getState() == AccountEnum.State.DESTROY) {
                log.info("{}对应的账号{}为已注销状态，登录自动恢复", username, account.getId());
                account.setState(AccountEnum.State.ENABLE);
                account.setCancelTime(null);
//            accountApi.accountEdit(account, null);
            }
        }

        return accountList;
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
        AccountEnum.Identity identity = findIdentity(null, account);
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
        if (account == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }

        // 切换端须解析该账号在目标端的角色, 否则 login 内 identity.getClient() 会 NPE
        AccountEnum.Identity identity = findIdentityByClient(client, account);
        if (identity == null) {
            log.error("切换端失败：账号{}在端{}下无有效角色", accountId, client);
            throw new PlatformException(AccountErrorCode.NOT_AVAIL_ROLE);
        }

        //登录
        LoginRes loginRes = login(account, AuthEnum.Type.TOGGLE, identity);
        loginRes.setClient(CollUtil.newArrayList(client));
        return loginRes;
    }

    /**
     * 取账号在指定端下的首个角色
     *
     * <p>账号 {@code identityList} 含跨端全部角色, 切换端时须过滤出属于目标端的角色,
     * 取其首个作为登录角色</p>
     *
     * @param client  目标端
     * @param account 账号
     * @return 目标端下首个角色, 无则 null
     */
    private AccountEnum.Identity findIdentityByClient(AccountEnum.Client client, AccountRpcVO account) {
        List<String> roleIdList = StrUtil.split(account.getIdentityList(), ",");
        for (String roleId : roleIdList) {
            AccountEnum.Identity identity = AccountEnum.Identity.getByCode(NumberUtil.parseLong(roleId));
            if (identity != null && identity.getClient() == client) {
                return identity;
            }
        }
        return null;
    }

    /**
     * 根据指定的角色找可用角色, 若没有指定, 则获取最早的角色
     */
    public AccountEnum.Identity findIdentity(AccountEnum.Identity companyRole, AccountRpcVO account) {
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

    public LoginRes login(AccountRpcVO account, AuthEnum.Type loginType, AccountEnum.Identity mainIdentity) {
        LoginRes loginRes = new LoginRes();
        AccountEnum.Client client = mainIdentity.getClient();
        // 生成 token: loginId 以 accountId#clientCode 拼接, StpInterface 按 # 切割出两段
        // 早前用数值加法 account.getId() + client.getCode() 会把 client code 当数字累加, 破坏 accountId, 且解析端转 Long 报错
        StpUtil.login(account.getId() + "#" + client.getCode(), SaLoginConfig
                .setExtra(TokenConstants.DETAILS_CLIENT, client.getCode())
                .setExtra(TokenConstants.DETAILS_ACCOUNT_ID, account.getId())
                .setExtra(TokenConstants.DETAILS_USERNAME, account.getUsername())
                .setExtra(TokenConstants.DETAILS_NICKNAME, account.getNickname())
                .setExtra(TokenConstants.DETAILS_UP_ID, account.getInviteAccountId())
                .setExtra(TokenConstants.DETAILS_IDENTITY, account.getIdentityList())
        );

        loginRes.setIdentity(mainIdentity);
        loginRes.setToken(StpUtil.getTokenValue());
        LoginAccountRes accountVO = loginAssembler.vo2LoginRes(account);
        loginRes.setAccountVO(accountVO);
        // 供应商端登录回填供应商状态(登录/切换端/注册后登录统一走此处, 业务只在一处)
        accountFill(loginRes, account.getId(), client);
        // mq
        loginNotify(account, loginType);
        return loginRes;
    }

    /**
     * 登录结果补充端相关业务态, 供登录/切换端/注册后登录复用
     *
     * <p>当前仅供应商端: 供应商注册后状态为 INIT(未开通), 前端据此转角色申请页;
     * 非供应商端无需补充。集中于此保证该业务逻辑只有一处。</p>
     *
     * @param loginRes  登录结果 (原地填充)
     * @param accountId 账号ID
     * @param client    登录端
     */
    private void accountFill(LoginRes loginRes, Long accountId, AccountEnum.Client client) {
        if (client == AccountEnum.Client.SUPPLIER) {
            SupplierOutVO supplier = supplierApi.supplier(accountId);
            if (supplier != null) {
                loginRes.setSupplierState(supplier.getState());
                // 非强制业务态
                loginRes.setPromisePayAuditState(AuditEnum.State.getByCode(supplier.getPromisePayAuditState()));
            }
        }
    }

    @Async
    public void loginNotify(AccountRpcVO account, AuthEnum.Type loginType) {
        //修改上次登录时间
        Long accountId = account.getId();
        //记录登录日志
        if (loginType.isNeedLog() && accountApi.updateLoginTime(account.getClient(), accountId)) {
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
    public LoginRes register(AccountEnum.Client client, RegisterReq customSaveReq) {
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
        return login(account, AuthEnum.Type.CODE, customSaveReq.getIdentity());
    }

    /**
     * 执行注册
     *
     * @param req 注册请求
     * @return 新账号 ID
     */
    private AccountRpcVO doRegister(AccountEnum.Client client, RegisterReq req) {
        if (StrUtil.isBlank(req.getUsername()) && StrUtil.isBlank(req.getPhone())) {
            throw new PlatformException(AccountErrorCode.REGISTER_CERTIFICATE_ERROR, "注册");
        }
        AccountEnum.Identity identity = req.getIdentity();
        if (identity == null) {
            identity = AccountEnum.Identity.getByCode(client.getDefaultIdentityCode());
            req.setIdentity(identity);
        }

        // 处理username和phone的数据同步关系
        String username = StrUtil.blankToDefault(req.getUsername(), req.getPhone());
        req.setUsername(username);
        if (PhoneUtil.isPhone(username) && StrUtil.isBlank(req.getPhone())) {
            req.setPhone(username);
        }

        List<AccountRpcVO> accountRpcVOList = accountApi.accountList(null, username);
        AccountRpcVO accountTemplate = CollUtil.getFirst(accountRpcVOList);
        AccountRpcVO existAccount = accountRpcVOList.stream().filter(it -> it.getClient() == client).findFirst().orElse(null);

        IdentityRegisterRpcReq registerRpcReq = TransferUtils.transfer(req, IdentityRegisterRpcReq.class);
        if (accountTemplate != null) {
            registerRpcReq.setId(accountTemplate.getId());
        }
        // 是否首次注册: 无同名账号即首次; DESTROY(注销未回收)复用原 id 覆盖注册非首次
        boolean isRegisterOnce = true;
        if (existAccount != null) {
            // 封禁校验(登录/注册一致), 封禁不可复用
            assertNotBlocked(existAccount, "注册");
            if (existAccount.getState() == AccountEnum.State.ENABLE) {
                throw new PlatformException(AccountErrorCode.EXIST_USERNAME, "注册");
            }
            registerRpcReq.setPid(existAccount.getId());
            isRegisterOnce = false;
        }

        registerRpcReq.setRegisterOnce(isRegisterOnce);

        // 短信验证
        VerificationCodeReq codeReq = new VerificationCodeReq();
        codeReq.setPhone(req.getPhone());
        codeReq.setType(SmsEnum.Type.Register);
        codeReq.setCode(req.getCode());
        SmsMethod.verificationCode(codeReq);

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
            if (identity == AccountEnum.Identity.CHANNEL) {
                // 同步注册member
                // 检查是否已经注册
                CommonEnum.YesOrNo createMember = req.getCreateMember();
                if (createMember == CommonEnum.YesOrNo.YES) {
                    AccountEnum.Client memberClient = AccountEnum.Client.USER;
                    AccountRpcVO memberExistAccount = accountApi.account(memberClient, username);
                    if (memberExistAccount == null) {
                        IdentityRegisterRpcReq memberRegisterRpcReq = TransferUtils.transfer(registerRpcReq, IdentityRegisterRpcReq.class);
                        memberRegisterRpcReq.setClient(memberClient);
                        memberRegisterRpcReq.setIdentity(AccountEnum.Identity.MEMBER);
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
        AccountRpcVO account = accountApi.account(client, accountId);

        //检查验证码
        VerificationCodeReq codeReq = new VerificationCodeReq();
        codeReq.setPhone(account.getPhone());
        codeReq.setCode(req.getCode());
        codeReq.setType(SmsEnum.Type.UpdatePassword);
        SmsMethod.verificationCode(codeReq);

        //获取新密码
        String newPassword = new BCryptPasswordEncoder().encode(req.getNewPassword());
        //持久化
        AccountRpcVO accountEdit = new AccountRpcVO();
        accountEdit.setId(account.getId());
        accountEdit.setPassword(newPassword);
        accountApi.accountEdit(accountEdit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUsername(CodeUpdateUsernameReq req) {
        CommonUtil.validate(req);

        //查询用户信息
        Long accountId = req.getAccountId();
        AccountEnum.Client client = req.getClient();
        String newUsername = req.getNewUsername();
        AccountRpcVO account = accountApi.account(client, accountId);

        //校验新账号名称
        boolean exists = accountApi.exists(req.getNewUsername(), client);
        if (exists) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }

        // 若新账号名不是手机号,则使用旧账号的手机号
        String phone = PhoneUtil.isPhone(newUsername) ? newUsername : account.getPhone();
        //校验验证码
        VerificationCodeReq codeReq = new VerificationCodeReq();
        codeReq.setPhone(phone);
        codeReq.setCode(req.getCode());
        codeReq.setType(SmsEnum.Type.UpdateUsername);
        SmsMethod.verificationCode(codeReq);

        //持久化
        AccountRpcVO accountUpdate = new AccountRpcVO();
        accountUpdate.setId(account.getId());
        accountUpdate.setUsername(req.getNewUsername());
        accountApi.accountEdit(accountUpdate);
    }

}
