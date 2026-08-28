package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.BindRoleCommand;
import com.newzkl.platform.base.biz.account.action.cmd.CountCmd;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountDetailQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountKeyQuery;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.CancelMemberReq;
import com.newzkl.platform.base.biz.account.model.req.DestroyRoleReq;
import com.newzkl.platform.base.biz.account.model.req.SimpleAccountQuery;
import com.newzkl.platform.base.biz.account.model.req.UpdateMemberInfoCommand;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.biz.account.model.res.AppAccountVO;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.properties.UserProperties;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号
 *
 * <p>按主数据聚合: 一切以 account 为主数据的查询与自助操作统一收此。
 * 收编自旧 {@code AccountController}(详情/基础/简易列表)、旧 {@code AdapterController#userAccount}(按身份分发详情)、
 * 旧 {@code ConsumerInfoController}(手机号查询/注销/信息修改/app 视图)。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
@FuncPermission("账号")
public class AccountController {

    private final UserQueryService userQueryService;
    private final AccountDomain accountDomain;
    private final ChannelClientDomain channelClientDomain;
    private final UserClientDomain userClientDomain;
    private final AccountService accountService;

    /**
     * 账号详情(按 id 或手机号)
     *
     * <p>合并旧 {@code /detail}(按 id) 与 {@code /getByPhone}(按手机号): 二者出参同为 {@link AccountOutRes},
     * 底层 {@code accountOutVO} 两重载。按非空 key 分流 —— id 优先(端取当前登录端), 否则按 phone 定位账号,
     * 两者皆空抛参数异常。收编旧 {@code ConsumerInfoController#getByPhone}。</p>
     *
     * @param query 账号详情查询(id 或 phone, id 优先)
     * @return 账号外部视图
     */
    @PostMapping("/detail")
    public PlatformResult<AccountOutRes> detail(@Validated @RequestBody AccountDetailQuery query) {
        if (query.getId() != null) {
            return PlatformResult.success(userQueryService.accountOutVO(SecurityUtils.getClient(), query.getId()));
        }
        if (StrUtil.isNotEmpty(query.getPhone())) {
            return PlatformResult.success(userQueryService.accountOutVO(query.getPhone()));
        }
        ThrowsException.exception(BaseErrorCode.PARAM, "账号 id 与手机号不能同时为空");
        return null;
    }

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
    public PlatformResult<Page<SimpleAccountRes>> simplePage(@RequestBody SimpleAccountQuery accountQuery) {
        return PlatformResult.success(accountDomain.simpleAccountPage(accountQuery));
    }

    /**
     * 身份详情
     *
     * <p>按身份分发: 供应商查供应商详情, 渠道商查渠道商详情, 其他抛参数异常。
     * 收编自旧 {@code AdapterController#userAccount}</p>
     *
     * @param userAccount 身份详情入参(身份 + 账号 ID)
     * @return 对应身份详情视图
     */
    @PostMapping("/identityDetail")
    public PlatformResult<?> identityDetail(@RequestBody CountCmd.UserAccount userAccount) {
        if (AccountEnum.Identity.SUPPLIER == userAccount.getIdentity()) {
            return PlatformResult.success(userQueryService.supplierVO(userAccount.getAccountId()));
        } else if (AccountEnum.Identity.CHANNEL == userAccount.getIdentity()) {
            return PlatformResult.success(channelClientDomain.channel(userAccount.getAccountId()));
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
            return null;
        }
    }

    /**
     * 注销账号
     *
     * <p>仅注销 account, 身份表数据保留。验证码校验与注销落库在领域层完成。
     * 收编自旧 {@code ConsumerInfoController#cancelMember}, 语义与命名对齐为注销账号。</p>
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
     * 注销角色
     *
     * <p>注销目标由 {@link DestroyRoleReq#getId()} 决定:</p>
     * <ul>
     *   <li>非 admin 操作人: 忽略入参 id, 以当前登录 accountId 覆写, 自助注销本账号, 需传短信验证码。</li>
     *   <li>admin 操作人: 必须传 id 指定被注销账号, 后端赋通行码绕过短信校验。</li>
     * </ul>
     *
     * @param destroyRoleReq 注销角色请求(目标 id + 身份 + 验证码 + 注销原因)
     * @return 空结果
     */
    @PostMapping("/destroy")
    @FuncPermission("注销账号")
    public PlatformResult<Void> destroy(@Validated @RequestBody DestroyRoleReq destroyRoleReq) {
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
     * 修改账号信息
     *
     * <p>收编自旧 {@code ConsumerInfoController#updateMemberInfo}</p>
     *
     * @param updateMemberInfoCommand 账号信息修改命令
     * @return 空结果
     */
    @PostMapping("/updateInfo")
    @FuncPermission("修改账号信息")
    public PlatformResult<Void> updateInfo(@Validated @RequestBody UpdateMemberInfoCommand updateMemberInfoCommand) {
        userClientDomain.updateMemberInfo(SecurityUtils.getAccountId(), updateMemberInfoCommand);
        return PlatformResult.success();
    }

    /**
     * app 账号信息
     *
     * <p>收编自旧 {@code ConsumerInfoController#app}</p>
     *
     * @param accountId 账号ID, 不传取当前登录账号
     * @return app 账号视图
     */
    @PostMapping("/app")
    public PlatformResult<AppAccountVO> app(@RequestParam(required = false) Long accountId) {
        AccountKeyQuery query = AccountKeyQuery.build();
        if (accountId != null) {
            query.setAccountId(accountId);
        }
        return PlatformResult.success(userQueryService.appVO(query));
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
     * 绑定角色到账号
     *
     * <p>全量替换指定账号的角色集合并重算派生权限。端由当前登录 client 推导, 角色须同端。
     * 迁移自 adopt-chicken {@code EmpController#bindRoles}, 语义对齐并适配中台多端隔离。</p>
     *
     * @param id  账号ID
     * @param cmd 角色ID集合
     * @return 空结果
     */
    @PostMapping("/{id}/bindRoles")
    @FuncPermission("绑定角色")
    public PlatformResult<Void> bindRoles(@PathVariable Long id, @RequestBody BindRoleCommand cmd) {
        accountService.bindRoles(id, cmd.roleIds());
        return PlatformResult.success();
    }
}
