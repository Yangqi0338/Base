package com.newzkl.platform.base.biz.auth.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountLoginService;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.auth.model.oauth.req.*;
import com.newzkl.platform.base.biz.auth.model.oauth.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.auth.model.oauth.res.LoginRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    private final AccountLoginService accountLoginService;

    /**
     * 切换身份
     *
     * @param toggleClientReq 切换端请求
     * @return 登录结果
     */
    @PostMapping("/toggleClient")
    public PlatformResult<LoginRes> toggleClient(@Validated @RequestBody ToggleClientReq toggleClientReq) {
        return PlatformResult.success(accountLoginService.toggleClient(toggleClientReq));
    }

    /**
     * 注册
     *
     * @param registerReq 注册请求
     * @return 登录结果(login=false 时 token 为空)
     */
    @PostMapping("/register")
    public PlatformResult<LoginRes> register(@Validated @RequestBody RegisterReq registerReq) {
        return PlatformResult.success(accountLoginService.register(SecurityUtils.getRequestClient(), registerReq));
    }


    /**
     * 刷新token
     *
     * @return 登录结果
     */
    @PostMapping("/refreshToken")
    public PlatformResult<LoginRes> refreshToken() {
        return PlatformResult.success(accountLoginService.refreshToken(SecurityUtils.getAccountId()));
    }

    /**
     * 登录
     *
     * @param loginReq 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public PlatformResult<LoginRes> login(@Validated @RequestBody LoginReq loginReq) {

        return PlatformResult.success(accountLoginService.accountLogin(SecurityUtils.doGetRequestClient(), loginReq));
    }

    /**
     * 登录并注册
     *
     * <p>先登录, 仅当账号不存在时后端自动注册后再登录; 注册回退开关由后端固定,
     * 不由前端入参控制, 与纯 {@code /login} 分为两个独立端口。</p>
     *
     * @param loginReq 登录请求(注册所需字段复用登录入参)
     * @return 登录结果
     */
    @PostMapping("/loginRegister")
    public PlatformResult<LoginRes> loginRegister(@Validated @RequestBody LoginReq loginReq) {
        return PlatformResult.success(accountLoginService.loginRegister(SecurityUtils.doGetRequestClient(), loginReq));
    }

    /**
     * 验证码修改密码
     *
     * @param codeUpdatePasswordCommand 改密命令
     * @return 空结果
     */
    @PostMapping("/codeUpdatePassword")
    public PlatformResult<Void> editPassword(@Validated @RequestBody CodeUpdatePasswordCommand codeUpdatePasswordCommand) {
        CodeUpdatePasswordReq req = TransferUtils.transfer(codeUpdatePasswordCommand, CodeUpdatePasswordReq::new);
        req.setClient(SecurityUtils.getClient());
        accountLoginService.editPassword(null);
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
    @PostMapping("/updateUsername")
    public PlatformResult<Void> editUsername(@Validated @RequestBody CodeUpdateUsernameCommand codeUpdateUsernameCommand) {
        CodeUpdateUsernameReq req = TransferUtils.transfer(codeUpdateUsernameCommand, CodeUpdateUsernameReq::new);
        req.setClient(SecurityUtils.getClient());
        accountLoginService.editUsername(req);
        return PlatformResult.success();
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
    @GetMapping("/loginOut")
    public PlatformResult<Void> loginOut() {
        return PlatformResult.success();
    }
}
