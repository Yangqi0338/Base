package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.RoleCmd;
import com.newzkl.platform.base.biz.account.application.service.IdentityService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.CdkDomain;
import com.newzkl.platform.base.biz.account.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.req.ToCdkCommand;
import com.newzkl.platform.base.biz.account.model.cdk.res.CdkRes;
import com.newzkl.platform.base.biz.account.model.req.RoleApplyCommand;
import com.newzkl.platform.base.biz.account.model.req.RoleQuery;
import com.newzkl.platform.base.biz.account.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.account.model.role.res.RoleRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountRoleVO;
import com.newzkl.platform.base.biz.account.model.vo.PromiseFlowVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-角色控制器。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.RoleController}, 路径与 HTTP 方法保持不变。
 * 旧控制器注入了 6 个同类型 {@code IUserQueryService} / {@code IRoleService} 别名字段, 本仓收敛为
 * {@link UserQueryService} + {@link IdentityService} 两个依赖。
 * 旧权限点 {@code @Limit(code = FuncCons.Admin.company_role)} 不在本层声明, 鉴权切面归入口 starter。
 * 旧 {@code SecurityUtils.getRole()} 在中台通用层已不存在, 改用 {@link SecurityUtils#getRoleId()}。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleDomain roleDomain;
    private final CdkDomain cdkDomain;
    private final UserQueryService userQueryService;
    private final IdentityService identityService;

    /**
     * 申请角色。
     *
     * @param roleApplyCommand 角色申请资料
     * @return 审批流 ID (审批域未接线时为 null)
     */
    @PostMapping("/applyRole")
    public PlatformResult<Long> applyRole(@RequestBody RoleApplyCommand roleApplyCommand) {
        return PlatformResult.success(identityService.applyRole(roleApplyCommand));
    }

    /**
     * 保存申请资料。
     *
     * @param roleApplyCommand 角色申请资料
     * @return 成功结果
     */
    @PostMapping("/saveApplyCommand")
    public PlatformResult<Void> saveApplyCommand(@RequestBody RoleApplyCommand roleApplyCommand) {
        identityService.saveApplyCommand(roleApplyCommand);
        return PlatformResult.success();
    }

    /**
     * 加载申请资料。
     *
     * <p>保留旧语义: 加载异常一律吞掉并降级返回 null, 仅打 warn 日志。</p>
     *
     * @param roleId 角色 ID
     * @return 角色申请资料, 无或异常时为 null
     */
    @GetMapping("/loadApplyCommand")
    public PlatformResult<RoleApplyCommand> loadApplyCommand(@RequestParam("roleId") Long roleId) {
        RoleApplyCommand roleApplyCommand = null;
        try {
            roleApplyCommand = identityService.loadApplyCommand(roleId);
        } catch (Exception e) {
            log.warn("加载申请资料异常");
        }
        return PlatformResult.success(roleApplyCommand);
    }

    /**
     * 账号角色信息。
     *
     * @return 当前登录账号已开通的角色列表
     */
    @PostMapping("/userRoleInfo")
    public PlatformResult<List<AccountRoleVO>> userRoleInfo() {
        return PlatformResult.success(userQueryService.accountRoleVO(SecurityUtils.getAccountId()));
    }

    /**
     * 提交保证金缴纳信息。
     *
     * @param promiseFlowVO 保证金缴纳流水
     * @return 审批流 ID (审批域未接线时为 null)
     */
    @PostMapping("/submitPromiseFlow")
    public PlatformResult<Long> submitPromiseFlow(@RequestBody @Valid PromiseFlowVO promiseFlowVO) {
        return PlatformResult.success(identityService.submitPromiseFlow(promiseFlowVO));
    }

    /**
     * 角色列表。
     *
     * @param roleQuery 角色查询
     * @return 角色列表
     */
    @PostMapping("/roleList")
    public PlatformResult<List<RoleRes>> roleList(@RequestBody RoleQuery roleQuery) {
        return PlatformResult.success(roleDomain.list(roleQuery));
    }

    /**
     * 角色详情。
     *
     * @param roleId 角色 ID
     * @return 角色详情, 无则 null
     */
    @GetMapping("/roleDetail")
    public PlatformResult<RoleRes> roleDetail(@RequestParam("roleId") Long roleId) {
        return PlatformResult.success(roleDomain.detail(roleId));
    }

    /**
     * 角色保存。
     *
     * <p>保留旧语义: 请求体带 id 走修改, 否则走新建。</p>
     *
     * @param roleReq 角色入参
     * @return 成功结果
     */
    @PostMapping("/roleListSave")
    public PlatformResult<Void> roleListSave(@RequestBody RoleReq roleReq) {
        if (roleReq.getId() != null) {
            roleDomain.edit(roleReq.getId(), roleReq);
        } else {
            roleDomain.save(roleReq);
        }
        return PlatformResult.success();
    }

    /**
     * 开通码列表。
     *
     * <p>保留旧语义: 平台角色不加数据范围限制, 运营商 / 交易师 / 渠道商分别按登录账号
     * 注入各自的归属条件。</p>
     *
     * @param cdkQuery 开通码查询
     * @return 开通码分页
     */
    @PostMapping("cdkList")
    public PlatformResult<Page<CdkRes>> cdkList(@RequestBody CdkQuery cdkQuery) {
        RoleEnum.CompanyRole role = RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId());
        Long accountId = SecurityUtils.getAccountId();
        if (RoleEnum.CompanyRole.OPERATOR == role) {
            cdkQuery.setOperatorId(accountId);
        } else if (RoleEnum.CompanyRole.DEALER == role) {
            cdkQuery.setDealerId(accountId);
        } else if (RoleEnum.CompanyRole.CHANNEL == role) {
            cdkQuery.setChannelId(accountId);
        }
        return PlatformResult.success(userQueryService.cdkPage(cdkQuery));
    }

    /**
     * 分配开通码。
     *
     * <p>分配人角色与账号取自登录态。</p>
     *
     * @param toCdkCommand 分配命令
     * @return 成功结果
     */
    @PostMapping("toCdk")
    public PlatformResult<Void> toCdk(@Validated @RequestBody ToCdkCommand toCdkCommand) {
        toCdkCommand.setFromRole(SecurityUtils.getRoleId());
        toCdkCommand.setFromUserId(SecurityUtils.getAccountId());
        identityService.toCdk(toCdkCommand);
        return PlatformResult.success();
    }

    /**
     * 修改开通码兑换状态。
     *
     * @param stateEdit 状态修改入参
     * @return 成功结果
     */
    @PostMapping("cdkStateEdit")
    public PlatformResult<Void> cdkStateEdit(@Validated @RequestBody RoleCmd.StateEdit stateEdit) {
        cdkDomain.cdkStateEdit(stateEdit.getId(), stateEdit.getUseState());
        return PlatformResult.success();
    }
}
