package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.application.service.IdentityService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.model.req.RoleApplyCommand;
import com.newzkl.platform.base.biz.account.model.vo.AccountRoleVO;
import com.newzkl.platform.base.biz.account.model.vo.PromiseFlowVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-角色
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.RoleController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中同一控制器内混用前导斜杠的写法
 * (如 {@code /applyRole} 与 {@code cdkList})。</p>
 *
 * <p>归属未迁清单 (领域越界, 拒绝迁入本域):</p>
 * <ul>
 *   <li>{@code POST /user/role/roleList}</li>
 *   <li>{@code GET /user/role/roleDetail}</li>
 *   <li>{@code POST /user/role/roleListSave}</li>
 * </ul>
 * <p>上述三端点操作 {@code role} 表, 其 DO/DAO/Repository/Domain 已落 biz-auth,
 * 按架构红线 (action 不得跨域 import 他域 domain 包) 应在 biz-auth 的 action 层承载。</p>
 *
 * <p>infra-gap 清单:</p>
 * <ul>
 *   <li>{@code POST /user/role/userRoleInfo}: 缺账号角色聚合查询 (旧
 *       {@code AccountDAO.xml#accountRoleVO}, supplier / channel 两表 UNION),
 *       中台仅迁了出参模型 {@code AccountRoleVO}, 无仓储与服务方法</li>
 * </ul>
 *
 * <p>鉴权说明: 旧 {@code roleListSave} 带 {@code @Limit(code=company_role, level=set)},
 * 该端点未迁入; 本仓 {@code StpInterface} 尚无实现, 方法级鉴权整体不生效, 见迁移报告「鉴权降级」。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user/role")
@RequiredArgsConstructor
public class RoleController {

    private final IdentityService identityService;
    private final UserQueryService userQueryService;

    /**
     * 申请角色
     *
     * @param roleApplyCommand 角色申请入参
     * @return 申请单ID
     */
    @PostMapping("/applyRole")
    public PlatformResult<Long> applyRole(@RequestBody RoleApplyCommand roleApplyCommand) {
        return PlatformResult.success(identityService.applyRole(roleApplyCommand));
    }

    /**
     * 保存申请资料
     *
     * @param roleApplyCommand 角色申请入参
     * @return 空结果
     */
    @PostMapping("/saveApplyCommand")
    public PlatformResult<Void> saveApplyCommand(@RequestBody RoleApplyCommand roleApplyCommand) {
        identityService.saveApplyCommand(roleApplyCommand);
        return PlatformResult.success();
    }

    /**
     * 加载申请资料
     *
     * <p>保留旧语义: 读取异常只告警不抛出, 返回 null。</p>
     *
     * @param roleId 角色ID
     * @return 角色申请资料, 无或异常则 null
     */
    @GetMapping("/loadApplyCommand")
    public PlatformResult<RoleApplyCommand> loadApplyCommand(@RequestParam("roleId") Long roleId) {
        RoleApplyCommand roleApplyCommand = null;
        try {
            roleApplyCommand = identityService.loadApplyCommand(roleId);
        } catch (Exception e) {
            log.warn("加载申请资料异常", e);
        }
        return PlatformResult.success(roleApplyCommand);
    }

    /**
     * 账号角色信息
     *
     * @return 账号角色列表
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("/userRoleInfo")
    public PlatformResult<List<AccountRoleVO>> userRoleInfo() {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 账号角色聚合查询未迁 — 缺 supplier / channel 两表 UNION 的仓储方法 "
                        + "(旧 AccountDAO.xml#accountRoleVO), 中台仅有出参模型 AccountRoleVO");
    }

    /**
     * 提交保证金缴纳信息
     *
     * @param promiseFlowVO 保证金流水
     * @return 流水ID
     */
    @PostMapping("/submitPromiseFlow")
    public PlatformResult<Long> submitPromiseFlow(@RequestBody @Valid PromiseFlowVO promiseFlowVO) {
        return PlatformResult.success(identityService.submitPromiseFlow(promiseFlowVO));
    }

}
