package com.newzkl.platform.base.biz.auth.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountLoginService;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.auth.model.oauth.req.*;
import com.newzkl.platform.base.biz.auth.model.oauth.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginRes;
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户-账号
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.AccountController}。
 * 类级路径与方法级路径逐字沿用旧契约: 类级为 {@code /user}, 方法级自带二级段 {@code /account/**}
 * 与 {@code /emp/**}, 不做合并改写。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


//    private final UserQueryService userQueryService;
//    private final AccountDomain accountDomain;
    private final AccountLoginService accountLoginService;
//    private final UserClientDomain userClientDomain;
//
    /**
     * 切换身份
     *
     * @param toggleClientReq 切换端请求
     * @return 登录结果
     */
    @PostMapping("/account/toggleClient")
    public PlatformResult<LoginRes> toggleClient(@Validated @RequestBody ToggleClientReq toggleClientReq) {
        return PlatformResult.success(accountLoginService.toggleClient(toggleClientReq));
    }

    /**
     * 密码登录
     *
     * <p>迁移补充: 旧 {@code accountLoginDomain.passwordLogin(PasswordLoginReq)} 已并入统一
     * {@code accountLogin(LoginReq)}, 登录方式由 {@code AccountEnum.LoginType} 区分,
     * 密码校验仍在应用层比对 BCrypt 摘要, 未简化。</p>
     *
     * @param loginReq 登录请求
     * @return 登录结果
     */
    @PostMapping("/account/passwordLogin")
    public PlatformResult<LoginRes> passwordLogin(@Validated @RequestBody LoginReq loginReq) {
        loginReq.setType(AccountEnum.LoginType.PASSWORD);
        return PlatformResult.success(accountLoginService.accountLogin(loginReq));
    }

    /**
     * 验证码注册（带登录）
     *
     * <p>保留旧语义: 角色强制为 c 端客户; 账号不存在时走注册。</p>
     *
     * @param codeLoginRegisterReq 验证码登录注册请求
     * @return 登录结果
     */
    @PostMapping("/codeRegister")
    public PlatformResult<LoginRes> codeLogin(@Validated @RequestBody CodeLoginRegisterReq codeLoginRegisterReq) {
        codeLoginRegisterReq.setRole(RoleEnum.CompanyRole.MEMBER);
        return PlatformResult.success(accountLoginService.loginRegister(codeLoginRegisterReq));
    }


    /**
     * 刷新token
     *
     * @return 登录结果
     */
    @PostMapping("/account/refreshToken")
    public PlatformResult<LoginRes> refreshToken() {
        return PlatformResult.success(accountLoginService.refreshToken(SecurityUtils.getAccountId()));
    }

    /**
     * 验证码登录
     *
     * <p>迁移补充: 旧 {@code accountLoginDomain.codeLogin(CodeLoginReq)} 已并入统一
     * {@code accountLogin(LoginReq)}, 验证码校验经账号仓储 {@code verificationCode} 完成。</p>
     *
     * @param loginReq 登录请求
     * @return 登录结果
     */
    @PostMapping("/account/codeLogin")
    public PlatformResult<LoginRes> codeLogin(@Validated @RequestBody LoginReq loginReq) {
        loginReq.setType(AccountEnum.LoginType.CODE);
        return PlatformResult.success(accountLoginService.accountLogin(loginReq));
    }

    /**
     * 注册不含登录
     *
     * <p>迁移补充: 旧实现经 {@code AbsRolePolicyFactory.getPolicy(roleId).customRegister} 分发,
     * 中台化后由应用层 {@code customeRegister} 内部按角色选策略, 手机号去重校验与错误码抛出保持一致。</p>
     *
     * @param customSaveReq 注册请求
     * @return 账号ID
     */
    @PostMapping("/account/codeRegister")
    public PlatformResult<Long> codeRegister(@Validated @RequestBody IdentityCustomSaveReq customSaveReq) {
        return PlatformResult.success(accountLoginService.customeRegister(customSaveReq));
    }

    /**
     * 验证码修改密码
     *
     * <p>迁移补充: 旧实现在 controller 内按 {@code mainAccountId} 分流主/子账号改密,
     * 中台化后统一由应用层 {@code editPassword} 承担; 验证码校验与 BCrypt 摘要写入均在领域层完成, 未简化。
     * 旧 {@code @Limit(code="0", level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param codeUpdatePasswordCommand 改密命令
     * @return 空结果
     */
    @PostMapping("/account/codeUpdatePassword")
    public PlatformResult<Void> editPassword(@Validated @RequestBody CodeUpdatePasswordCommand codeUpdatePasswordCommand) {
        CodeUpdatePasswordReq req = TransferUtils.transfer(codeUpdatePasswordCommand, CodeUpdatePasswordReq::new);
        accountLoginService.editPassword(req);
        return PlatformResult.success();
    }

    /**
     * 修改账号名称
     *
     * <p>迁移补充: 旧实现按 {@code mainAccountId} 分流主/子账号改名, 中台化后统一由应用层
     * {@code editUsername} 承担, 新账号名重复校验保留在领域层。</p>
     *
     * @param codeUpdateUsernameCommand 改名命令
     * @return 空结果
     */
    @PostMapping("/account/updateUsername")
    public PlatformResult<Void> editUsername(@Validated @RequestBody CodeUpdateUsernameCommand codeUpdateUsernameCommand) {
        CodeUpdateUsernameReq req = TransferUtils.transfer(codeUpdateUsernameCommand, CodeUpdateUsernameReq::new);
        accountLoginService.editUsername(req);
        return PlatformResult.success();
    }

    /**
     * 可选的身份
     *
     * <p>迁移补充: 旧实现在 controller 内按端分组拼装角色并按 {@code isAll}/{@code type} 过滤,
     * 中台化后归入 {@code accountDomain.accountRoleList}, 其内部已完成按端分组与展示名处理。
     * 旧的 {@code isAll}/{@code type} 两个查询参数为兼容保留, 领域层不再区分。</p>
     *
     * @param isAll 是否含当前端身份
     * @param type  分组类型
     * @return 身份列表
     */
    @PostMapping("/account/roleList")
    public PlatformResult<List<RoleVO>> accountAppVO(
            @RequestParam(required = false, defaultValue = "false") Boolean isAll,
            @RequestParam(required = false, defaultValue = "0") Integer type
    ) {
        return PlatformResult.success(accountLoginService.accountRoleList(SecurityUtils.getAccountId(), SecurityUtils.getClient()));
    }

    /**
     * 登录记录分页
     *
     * @param accountLoginLogQuery 登录记录查询
     * @return 登录记录分页
     */
    @PostMapping("accountLoginLogPage")
    public PlatformResult<Page<AccountLoginLogRes>> accountLoginLogPage(@RequestBody AccountLoginLogQuery accountLoginLogQuery) {
        accountLoginLogQuery.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(accountLoginService.accountLoginLogPage(accountLoginLogQuery));
    }

    /**
     * 退出登录
     *
     * <p>迁移补充: 旧实现直接经 {@code RedisUtil.del} 清网关权限缓存, 属基础设施动作,
     * action 层不得直连缓存; biz-account 领域暂无登出方法, 故此端点当前仅保留契约与返回结构,
     * 未执行缓存清理, 见迁移报告「能力缺失」。</p>
     *
     * @return 空结果
     */
    @GetMapping("/account/loginOut")
    public PlatformResult<Void> loginOut() {
        return PlatformResult.success();
    }
}
