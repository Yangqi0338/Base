package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.CountCmd;
import com.newzkl.platform.base.biz.account.model.req.SubAccountQuery;
import com.newzkl.platform.base.biz.account.model.req.SubAccountSaveReq;
import com.newzkl.platform.base.biz.account.model.res.SubAccountDetailRes;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.CancelMemberReq;
import com.newzkl.platform.base.biz.account.model.req.DestroyRoleReq;
import com.newzkl.platform.base.biz.account.model.req.SimpleAccountQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountAggRes;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.properties.UserProperties;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号
 *
 * <p>按主数据聚合: 一切以 account 为主数据的查询与自助操作统一收此。
 * 收编自旧 {@code AccountController}、旧 {@code AdapterController#userAccount}、旧 {@code ConsumerInfoController}。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
@FuncPermission("账号")
public class AccountController {

    private final AccountDomain accountDomain;
    private final UserClientDomain userClientDomain;
    private final AccountService accountService;

    /**
     * 账号基础信息
     *
     * @return 当前登录账号视图
     */
    @PostMapping("/base")
    public PlatformResult<AccountVO> base() {
        return PlatformResult.success(accountDomain.account(SecurityUtils.getClient(), SecurityUtils.getAccountId()));
    }

    /**
     * 简易账号列表
     *
     * @param accountQuery 简易账号查询
     * @return 简易账号分页
     */
    @PostMapping("/simplePage")
    @RoleLimit(client = {AccountEnum.Client.ADMIN})
    public PlatformResult<Page<SimpleAccountRes>> simplePage(@RequestBody SimpleAccountQuery accountQuery) {
        return PlatformResult.success(accountDomain.simpleAccountPage(accountQuery));
    }

    /**
     * 账号 + 身份聚合分页
     * <p>
     * 身份来源分两支:
     * <ul>
     *     <li>admin 操作人: identityList 由 body 传入, 可传多个身份跨端聚合, 传了哪些身份就回填哪些槽</li>
     *     <li>非 admin 操作人: 忽略 body 里的 identityList, 一律以 token 中的身份覆写, 不信前端</li>
     * </ul>
     * account 不按 client 过滤 (跨端), 只按 identityList 过滤; 角色按账号各自 client 分组查编码列表。
     *
     * @param query 聚合分页入参
     * @return 每行 = 账号主体 (不含密码) + 角色编码列表 + 身份槽
     */
    @PostMapping("/aggPage")
    @FuncPermission("账号聚合分页")
    public PlatformResult<Page<AccountAggRes>> aggPage(@RequestBody AccountQuery query) {
        if (AccountEnum.Client.ADMIN != SecurityUtils.getClient()) {
            query.setIdentity(SecurityUtils.getIdentity());
        }
        return PlatformResult.success(accountService.aggPage(query));
    }

    /**
     * 账号 + 身份聚合详情
     * <p>
     * 非 admin 操作人忽略 body 里的 identityList, 以 token 身份覆写; admin 传什么身份回填什么槽。
     *
     * @param userAccount 身份列表 + 账号 ID
     * @return 账号主体 (不含密码) + 角色编码列表 + 身份槽
     */
    @PostMapping("/identityDetail")
    public PlatformResult<AccountAggRes> identityDetail(@RequestBody @Validated CountCmd.UserAccount userAccount) {
        if (AccountEnum.Client.ADMIN != SecurityUtils.getClient()) {
            userAccount.setIdentityList(List.of(SecurityUtils.getIdentity()));
        }
        return PlatformResult.success(accountService.aggDetail(userAccount.getIdentityList(),
                userAccount.getAccountId()));
    }

    /**
     * 注销账号 (C 端会员)
     *
     * <p>仅注销 account (改 state=DESTROY), 不注销身份 (身份表数据保留)。
     * 验证码校验与注销落库在领域层完成。收编自旧 {@code ConsumerInfoController#cancelMember}。</p>
     *
     * @param command 注销命令
     * @return 空结果
     */
    @PostMapping("/cancelAccount")
    @FuncPermission("注销账号")
    public PlatformResult<Void> cancelAccount(@Validated @RequestBody CancelMemberReq command) {
        userClientDomain.cancelAccount(SecurityUtils.getAccountId(), command);
        return PlatformResult.success();
    }

    /**
     * 注销身份
     *
     * <p>注销目标由 {@link DestroyRoleReq#getId()} 决定:</p>
     * <ul>
     *   <li>非 admin 操作人: 忽略入参 id, 以当前登录 accountId 覆写, 自助注销本账号的一个身份, 需传短信验证码。</li>
     *   <li>admin 操作人: 必须传 id 指定被注销账号, 后端赋通行码绕过短信校验。</li>
     * </ul>
     *
     * @param destroyRoleReq 注销身份请求(目标 id + 身份 + 验证码 + 注销原因)
     * @return 空结果
     */
    @PostMapping("/destroyIdentity")
    @FuncPermission("注销身份")
    public PlatformResult<Void> destroyIdentity(@Validated @RequestBody DestroyRoleReq destroyRoleReq) {
        if (AccountEnum.Client.ADMIN == SecurityUtils.getClient()) {
            if (destroyRoleReq.getId() == null) {
                ThrowsException.exception(BaseErrorCode.PARAM, "管理端注销必须指定账号 id");
            }
            destroyRoleReq.setCode(UserProperties.passCode());
        } else {
            destroyRoleReq.setId(SecurityUtils.getAccountId());
        }
        accountDomain.destroy(destroyRoleReq.getId(), destroyRoleReq);
        return PlatformResult.success();
    }

    /**
     * 注销账号 (非 C 端)
     *
     * <p>整个注销账号 (改 state=DESTROY), 身份表数据保留。非 C 端前端专属, 任意账号注销,
     * 后端赋通行码绕过短信校验。C 端会员注销请走 /cancelAccount。</p>
     *
     * @param idCommand 账号 ID 入参
     * @return 空结果
     */
    @PostMapping("/destroyAccount")
    @FuncPermission("注销账号")
    public PlatformResult<Void> destroyAccount(@Validated @RequestBody IdCommand idCommand) {
        if (AccountEnum.Client.USER == SecurityUtils.getClient()) {
            ThrowsException.exception(BaseErrorCode.PARAM, "会员注销请走 cancelAccount");
        }
        if (idCommand.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "注销账号必须指定账号 id");
        }
        accountDomain.destroyAccount(idCommand.getId());
        return PlatformResult.success();
    }

    /**
     * 修改账号信息 (非 C 端)
     *
     * <p>只修改 account 内容 (昵称/真实姓名/手机号/头像等), 不改身份数据 —— 身份数据去对应身份 controller 修改。
     * 非 C 端前端专属, C 端会员修改走 C 端通道。</p>
     *
     * @param req 账号信息修改请求
     * @return 空结果
     */
    @PostMapping("/accountEdit")
    @FuncPermission("修改账号信息")
    public PlatformResult<Void> accountEdit(@Validated @RequestBody AccountReq req) {
        if (AccountEnum.Client.USER == SecurityUtils.getClient()) {
            ThrowsException.exception(BaseErrorCode.PARAM, "会员请走 C 端通道");
        }
        req.setId(SecurityUtils.getAccountId());
        req.setIdentity(SecurityUtils.getIdentity());
        accountDomain.accountEdit(req);
        return PlatformResult.success();
    }

    /**
     * 禁用或启用账号
     *
     * <p>收编自旧 {@code MemberController#disable}。仅处理 account 自身状态 (ENABLE/DISABLE),
     * 关联身份实体 (供应商/渠道商等) 状态不受影响。端由请求 client 指定, 未传则取当前登录端。</p>
     *
     * @param req 禁用/启用请求
     * @return 空结果
     */
    @PostMapping("/disable")
    @FuncPermission("禁用或启用账号")
    public PlatformResult<Object> disable(@Validated @RequestBody AdminDisableAccountReq req) {
        if (req.getClient() == null) {
            req.setClient(SecurityUtils.getClient());
        }
        accountService.disableAccount(req);
        return PlatformResult.success();
    }

    /**
     * 新增/编辑子账号
     *
     * <p>主账号创建编辑子账号(等同员工管理): id 空走新增, 继承主账号 identity,
     * pid=pidList头=mainAccountId=当前登录id, origin=MAIN_CREATE, password 必填,
     * username 主账号内唯一; id 非空走编辑, 归属校验后改昵称/手机号, password 非空则重置。
     * 两路均按 roleIds 全量替换端内角色。子账号自身改密/改名/注销由子账号自理(AuthController)。</p>
     *
     * @param req 子账号新增/编辑请求
     * @return 子账号id
     */
    @PostMapping("/sub/save")
    @FuncPermission("保存子账号")
    public PlatformResult<Long> subSave(@Validated @RequestBody SubAccountSaveReq req) {
        return PlatformResult.success(accountService.subSave(req));
    }

    /**
     * 子账号分页
     *
     * @param query 子账号查询(mainAccountId 由登录态注入)
     * @return 子账号详情分页
     */
    @PostMapping("/sub/page")
    public PlatformResult<Page<SubAccountDetailRes>> subPage(@RequestBody SubAccountQuery query) {
        return PlatformResult.success(accountService.subPage(query));
    }

    /**
     * 删除子账号
     *
     * <p>逻辑删除并清空角色关系, 完全由主账号控制。</p>
     *
     * @param id 子账号id
     * @return 空结果
     */
    @PostMapping("/sub/{id}/delete")
    @FuncPermission("删除子账号")
    public PlatformResult<Void> subDelete(@PathVariable Long id) {
        accountService.subDelete(id);
        return PlatformResult.success();
    }
}
