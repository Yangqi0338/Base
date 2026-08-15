package com.newzkl.platform.base.biz.auth.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.action.cmd.AssignPermissionCommand;
import com.newzkl.platform.base.biz.auth.action.cmd.BindAccountCommand;
import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;

import com.newzkl.platform.base.biz.auth.model.permission.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FuncPermission("角色管理")
public class RoleController {

    private final RoleDomain roleDomain;

    /**
     * 分页查询角色
     *
     * @param query 查询条件
     * @return 角色分页
     */
    @PostMapping("/page")
    @FuncPermission("角色分页")
    public PlatformResult<Page<RoleVO>> page(@RequestBody RoleQuery query) {
        return PlatformResult.success(roleDomain.page(query));
    }

    /**
     * 列出全部角色
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    @FuncPermission("角色列表")
    public PlatformResult<List<RoleVO>> list() {
        return PlatformResult.success(roleDomain.listAll());
    }

    /**
     * 角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    @GetMapping("/detail/{id}")
    @FuncPermission("角色详情")
    public PlatformResult<RoleVO> detail(@PathVariable Long id) {
        return PlatformResult.success(roleDomain.detail(id));
    }

    /**
     * 创建角色
     *
     * @param req 角色入参
     * @return 主键ID
     */
    @PostMapping("/create")
    @FuncPermission("创建角色")
    public PlatformResult<Long> create(@RequestBody @Valid RoleReq req) {
        return PlatformResult.success(roleDomain.create(req));
    }

    /**
     * 更新角色
     *
     * @param req 角色入参
     * @return 空结果
     */
    @PutMapping("/update")
    @FuncPermission("更新角色")
    public PlatformResult<Object> update(@RequestBody @Valid RoleReq req) {
        roleDomain.update(req);
        return PlatformResult.success();
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 空结果
     */
    @PostMapping("/delete/{id}")
    @FuncPermission("删除角色")
    public PlatformResult<Object> delete(@PathVariable Long id) {
        roleDomain.delete(id);
        return PlatformResult.success();
    }

    /**
     * 为角色分配权限
     *
     * @param id  角色ID
     * @param cmd 权限ID集合
     * @return 空结果
     */
    @PostMapping("/assignPermissions/{id}")
    @FuncPermission("分配权限")
    public PlatformResult<Object> assignPermissions(@PathVariable Long id, @RequestBody AssignPermissionCommand cmd) {
        roleDomain.assignPermissions(id, cmd.permissionIds());
        return PlatformResult.success();
    }

    /**
     * 绑定账号到角色
     *
     * @param id  角色ID
     * @param cmd 账号ID集合
     * @return 空结果
     */
    @PostMapping("/bindAccounts/{id}")
    @FuncPermission("绑定账号")
    public PlatformResult<Object> bindAccounts(@PathVariable Long id, @RequestBody BindAccountCommand cmd) {
        roleDomain.bindAccounts(id, cmd.accountIds());
        return PlatformResult.success();
    }
}
