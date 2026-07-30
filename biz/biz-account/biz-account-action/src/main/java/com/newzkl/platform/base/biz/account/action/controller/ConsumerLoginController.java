package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.application.auth.service.AccountLoginService;
import com.newzkl.platform.base.biz.account.model.auth.req.CodeLoginRegisterReq;
import com.newzkl.platform.base.biz.account.model.auth.req.LoginReq;
import com.newzkl.platform.base.biz.account.model.auth.res.LoginRes;
import com.newzkl.platform.base.biz.account.model.auth.res.TokenAndExpireRes;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.req.ResetMemberReq;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者-注册登录
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.ConsumerLoginController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移补充: 找回密码两步端点 {@code /resetPasswordSmsCode} 与
 * {@code /resetMemberPasswordUpdate} 已补齐, 安全链路 (AES 验签 / 时间戳有效期 / 限流 /
 * nonce 防重放 / 短信验证码 / 设备标记 / 密码强度) 逐环保留。
 * 旧另有单步端点 {@code /resetPassword} (依赖 {@code IAccountDomain.resetMemberPassword},
 * 登录态下按 accountId 直接重置), 不在本次补齐清单内, 仍未迁移。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/consume/login")
@RequiredArgsConstructor
public class ConsumerLoginController {

    private final AccountLoginService accountLoginService;

    /**
     * 密码登录
     *
     * <p>保留旧语义: 角色强制为 c 端客户。密码比对仍在应用层校验 BCrypt 摘要, 未简化。</p>
     *
     * @param loginReq 登录请求
     * @return 登录结果
     */
    @PostMapping("/passwordLogin")
    public PlatformResult<LoginRes> passwordLogin(@Validated @RequestBody LoginReq loginReq) {
        loginReq.setRole(RoleEnum.CompanyRole.MEMBER);
        loginReq.setType(AccountEnum.LoginType.PASSWORD);
        return PlatformResult.success(accountLoginService.accountLogin(loginReq));
    }

    /**
     * 验证码登录包含注册
     *
     * <p>保留旧语义: 角色强制为 c 端客户; 账号不存在时走注册。</p>
     *
     * @param codeLoginRegisterReq 验证码登录注册请求
     * @return 登录结果
     */
    @PostMapping("/codeLogin")
    public PlatformResult<LoginRes> codeLogin(@Validated @RequestBody CodeLoginRegisterReq codeLoginRegisterReq) {
        codeLoginRegisterReq.setRole(RoleEnum.CompanyRole.MEMBER);
        return PlatformResult.success(accountLoginService.loginRegister(codeLoginRegisterReq));
    }

    /**
     * 按账号ID取token与有效期
     *
     * <p>迁移补充: 旧签名只收 {@code accountId}, 中台化后需带端类型, 此处补当前登录端;
     * 旧入参用 MyBatis 的 {@code @Param} 注解 (对 HTTP 绑定无效, 实际按同名 query 参数绑定),
     * 迁移后改为语义等价的 {@code @RequestParam}。</p>
     *
     * @param accountId 账号ID
     * @return token 与有效期
     */
    @GetMapping("/getTokenAndExpireById")
    public PlatformResult<TokenAndExpireRes> getTokenAndExpireById(@RequestParam("accountId") Long accountId) {
        return PlatformResult.success(accountLoginService.getTokenAndExpireById(accountId, SecurityUtils.getClient()));
    }

    /**
     * 重置会员密码 第一步 验证短信
     *
     * <p>出参类型沿用旧契约 {@code ScmResult<LoginRes>} 的形态 (旧实现同样恒为 null 数据体)</p>
     *
     * @param req 找回密码请求
     * @return 空结果
     */
    @PostMapping("/resetPasswordSmsCode")
    public PlatformResult<LoginRes> resetPasswordSmsCode(@Validated @RequestBody ResetMemberReq req) {
        accountLoginService.resetPasswordSmsCode(req);
        return PlatformResult.success();
    }

    /**
     * 重置会员密码 第二步 执行修改密码
     *
     * <p>出参类型沿用旧契约 {@code ScmResult<LoginRes>} 的形态 (旧实现同样恒为 null 数据体)</p>
     *
     * @param req 找回密码请求
     * @return 空结果
     */
    @PostMapping("/resetMemberPasswordUpdate")
    public PlatformResult<LoginRes> resetMemberPasswordUpdate(@Validated @RequestBody ResetMemberReq req) {
        accountLoginService.resetMemberPasswordUpdate(req);
        return PlatformResult.success();
    }
}
