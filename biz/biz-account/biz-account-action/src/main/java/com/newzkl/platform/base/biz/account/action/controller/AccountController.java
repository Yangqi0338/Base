package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.auth.service.AccountLoginService;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.CodeUpdatePasswordReq;
import com.newzkl.platform.base.biz.account.model.auth.req.CustomSaveBatchReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.LoginReq;
import com.newzkl.platform.base.biz.account.model.auth.req.SubPasswordLoginReq;
import com.newzkl.platform.base.biz.account.model.auth.req.ToggleClientReq;
import com.newzkl.platform.base.biz.account.model.auth.req.web.CodeUpdatePasswordCommand;
import com.newzkl.platform.base.biz.account.model.auth.res.LoginRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.account.model.req.AccountKeyQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountParentQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.CodeUpdateUsernameReq;
import com.newzkl.platform.base.biz.account.model.req.DestroyRoleReq;
import com.newzkl.platform.base.biz.account.model.req.SimpleAccountQuery;
import com.newzkl.platform.base.biz.account.model.req.SubEditReq;
import com.newzkl.platform.base.biz.account.model.req.SubProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.UpdateMemberInfoCommand;
import com.newzkl.platform.base.biz.account.model.req.cmd.CodeUpdateUsernameCommand;
import com.newzkl.platform.base.biz.account.model.res.AccountFinanceVO;
import com.newzkl.platform.base.biz.account.model.res.AccountLoginLogRes;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.biz.account.model.res.AppAccountVO;
import com.newzkl.platform.base.biz.account.model.res.AppHomePageDataVO;
import com.newzkl.platform.base.biz.account.model.res.RoleVO;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.res.SubAccountVO;
import com.newzkl.platform.base.biz.account.model.res.UserHomePageRes;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/user")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserQueryService userQueryService;
    private final AccountDomain accountDomain;
    private final AccountLoginService accountLoginService;
    private final UserClientDomain userClientDomain;

    /**
     * APP首页数据
     *
     * @return APP首页数据
     */
    @PostMapping("/account/appHomePageData")
    public PlatformResult<AppHomePageDataVO> appHomePageData() {
        return PlatformResult.success(accountService.appHomePageData(SecurityUtils.getAccountId()));
    }

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
     * 个人注册
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
     * 招募邀请二维码
     *
     * <p>迁移补充: 旧实现的二维码绘制逻辑本就整段注释, 仅回传邀请链接与空图 base64,
     * 此处逐字保留旧行为, 未补齐绘制能力。</p>
     *
     * @param code 邀请码
     * @param size 二维码边长
     * @return 含邀请链接与图片 base64 的映射
     */
    @GetMapping(value = "/qrcode/base64CodeRegister", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> qrBase64(@RequestParam String code,
                                        @RequestParam(defaultValue = "480") int size) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String base64 = Base64.getEncoder().encodeToString(out.toByteArray());
        Map<String, Object> resp = new HashMap<>();
        resp.put("inviteUrl", "https://aaa1.y.zzxyg88.com/invite?code=" + code);
        resp.put("imageBase64", "data:image/png;base64," + base64);
        return resp;
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
        accountDomain.editPassword(req);
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
        accountDomain.editUsername(req);
        return PlatformResult.success();
    }

    /**
     * 消费者修改账号信息
     *
     * @param updateMemberInfoCommand 会员信息修改命令
     * @return 空结果
     */
    @PostMapping("/account/updateMemberInfo")
    public PlatformResult<Void> updateMemberInfo(@Validated @RequestBody UpdateMemberInfoCommand updateMemberInfoCommand) {
        userClientDomain.updateMemberInfo(SecurityUtils.getAccountId(), updateMemberInfoCommand);
        return PlatformResult.success();
    }

    /**
     * 子账号密码登录
     *
     * @param subPasswordLoginReq 子账号登录请求
     * @return 登录结果
     */
    @PostMapping("/account/subPasswordLogin")
    public PlatformResult<LoginRes> subPasswordLogin(@Validated @RequestBody SubPasswordLoginReq subPasswordLoginReq) {
        LoginReq loginReq = TransferUtils.transfer(subPasswordLoginReq, LoginReq::new);
        loginReq.setType(AccountEnum.LoginType.PASSWORD);
        return PlatformResult.success(accountLoginService.subPasswordLogin(subPasswordLoginReq.getMainUsername(), loginReq));
    }

    /**
     * 账号详情
     *
     * @return 账号外部视图
     */
    @PostMapping("/account/accountDetail")
    public PlatformResult<AccountOutRes> account() {
        return PlatformResult.success(userQueryService.accountOutVO(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * 账号信息
     *
     * @return 账号视图
     */
    @PostMapping("/account/accountBase")
    public PlatformResult<AccountVO> accountVO() {
        return PlatformResult.success(accountDomain.account(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * app账号信息
     *
     * @param accountId 账号ID, 不传取当前登录账号
     * @return app账号视图
     */
    @PostMapping("/account/app")
    public PlatformResult<AppAccountVO> accountAppVO(@RequestParam(required = false) Long accountId) {
        AccountKeyQuery query = AccountKeyQuery.build();
        if (accountId != null) {
            query.setAccountId(accountId);
        }
        return PlatformResult.success(userQueryService.appVO(query));
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
        return PlatformResult.success(accountDomain.accountRoleList(SecurityUtils.getAccountId(), SecurityUtils.getClient()));
    }

    /**
     * 简易账号列表
     *
     * @param accountQuery 简易账号查询
     * @return 简易账号分页
     */
    @PostMapping("/account/simpleAccountPage")
    public PlatformResult<Page<SimpleAccountRes>> simpleAccountPage(@RequestBody SimpleAccountQuery accountQuery) {
        return PlatformResult.success(accountDomain.simpleAccountPage(accountQuery));
    }

    /**
     * 新增子账号
     *
     * @param subProxySaveReq 子账号新增请求
     * @return 空结果
     */
    @PostMapping("/emp/createEmp")
    public PlatformResult<Void> subProxySave(@Validated @RequestBody SubProxySaveReq subProxySaveReq) {
        accountDomain.proxySave(SecurityUtils.getAccountId(), subProxySaveReq);
        return PlatformResult.success();
    }

    /**
     * 编辑子账号
     *
     * @param subEditReq 子账号编辑请求
     * @return 空结果
     */
    @PostMapping("/emp/updateEmp")
    public PlatformResult<Void> subEditBase(@Validated @RequestBody SubEditReq subEditReq) {
        if (subEditReq.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        accountDomain.subEditBase(SecurityUtils.getAccountId(), subEditReq.getId(), subEditReq);
        return PlatformResult.success();
    }

    /**
     * 删除子账号
     *
     * @param idListCommand ID 列表
     * @return 空结果
     */
    @PostMapping("/emp/delete")
    public PlatformResult<Void> subAccountDelete(@Validated @RequestBody IdListCommand idListCommand) {
        accountDomain.accountDelete(idListCommand.getIdList());
        return PlatformResult.success();
    }

    /**
     * 子账号列表
     *
     * @param accountQuery 账号查询
     * @return 子账号列表
     */
    @PostMapping("/emp/pageEmp")
    public PlatformResult<List<AccountVO>> subAccountPage(@RequestBody AccountQuery accountQuery) {
        accountQuery.setMainAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(accountDomain.accountPage(accountQuery).getRecords());
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
     * 批量进行个人注册
     *
     * @param customSaveBatchReqList 批量注册请求
     * @return 空结果
     */
    @PostMapping("/account/customSaveBatch")
    public PlatformResult<LoginRes> customSaveBatch(@Validated @RequestBody List<CustomSaveBatchReq> customSaveBatchReqList) {
        accountLoginService.accountBatchRegister(customSaveBatchReqList);
        return PlatformResult.success();
    }

    /**
     * 用户收益统计
     *
     * @return 用户收益视图
     */
    @PostMapping("/account/finance")
    public PlatformResult<AccountFinanceVO> accountFinanceVO() {
        return PlatformResult.success(accountService.accountFinanceVO(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * 奖金池获取用户列表
     *
     * @param query 奖金池用户查询
     * @return 奖金池用户分页
     */

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

    /**
     * 账号列表
     *
     * <p>保留旧前置改写语义: {@code accountType} 为主账号时把 {@code mainAccountId} 钉为
     * {@link AccountEnum#MAIN_ACCOUNT_PID}; 平台角色额外把 {@code stateOver} 钉为
     * {@link AccountEnum.State#DESTROY}, 即平台可见含已销毁账号。</p>
     *
     * <p>迁移补充: 旧 {@code @EnableIdParse}(ID 明文/密文互转切面)未迁入 Base, 出参 ID 不再脱敏。</p>
     *
     * @param accountQuery 账号查询
     * @return 账号分页
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("/account/accountPage")
    public PlatformResult<Page<AccountVO>> accountPage(@RequestBody AccountQuery accountQuery) {
        if (AccountEnum.SubUserType.MAIN == accountQuery.getAccountType()) {
            accountQuery.setMainAccountId(AccountEnum.MAIN_ACCOUNT_PID);
        }
        if (RoleEnum.CompanyRole.PLATFORM.getCode().equals(SecurityUtils.getRoleId())) {
            accountQuery.setStateOver(AccountEnum.State.DESTROY);
        }
        return PlatformResult.success(accountDomain.accountPage(accountQuery));
    }

    /**
     * 下级用户列表
     *
     * <p>保留旧前置改写语义: 未传 {@code id} 取当前登录账号; 未传 {@code client} 取当前登录端;
     * 未传 {@code roleIdList} 取当前端的全部企业角色 ID。</p>
     *
     * @param query 下级账号查询
     * @return 下级账号列表
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("/account/subAccountList")
    public PlatformResult<List<SubAccountVO>> subAccountList(@RequestBody AccountParentQuery query) {
        if (query.getId() == null) {
            query.setId(SecurityUtils.getAccountId());
        }
        if (query.getClient() == null) {
            query.setClient(SecurityUtils.getClient());
        }
        if (CollUtil.isEmpty(query.getRoleIdList())) {
            query.setRoleIdList(RoleEnumUtil.findClientRoleIdList(SecurityUtils.getClient()));
        }
        return PlatformResult.success(accountDomain.subAccountList(query));
    }

    /**
     * 注销角色
     *
     * <p>迁移补充: 旧出参声明为 {@code ScmResult<LoginRes>} 但方法体只 {@code success()} 不带体,
     * 逐字保留该出参声明与空体行为。</p>
     *
     * @param destroyRoleReq 注销角色请求
     * @return 空结果 (出参类型沿用旧声明)
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("/account/destroy")
    public PlatformResult<LoginRes> destroy(@Validated @RequestBody DestroyRoleReq destroyRoleReq) {
        accountDomain.destroy(SecurityUtils.getAccountId(), destroyRoleReq);
        return PlatformResult.success();
    }

    /**
     * 脉脉通查看用户主页信息
     *
     * @param userId 被查看用户ID
     * @return 用户主页信息
     */
    @GetMapping("/account/userHomePage")
    public PlatformResult<UserHomePageRes> getUserHomePage(@RequestParam Long userId) {
        return PlatformResult.success(accountService.getUserHomePage(userId, SecurityUtils.getAccountId()));
    }
}
