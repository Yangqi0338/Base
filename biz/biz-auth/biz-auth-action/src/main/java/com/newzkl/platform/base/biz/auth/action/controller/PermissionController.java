package com.newzkl.platform.base.biz.auth.action.controller;

import com.newzkl.platform.base.biz.auth.action.service.PermissionSyncService;
import com.newzkl.platform.base.biz.auth.domain.service.PermissionDomain;
import com.newzkl.platform.base.biz.auth.model.enums.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionListDTO;
import com.newzkl.platform.base.biz.auth.model.permission.req.PermissionReq;
import com.newzkl.platform.base.biz.auth.model.permission.vo.PermissionTreeVO;
import com.newzkl.platform.base.biz.auth.model.permission.vo.PermissionVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/permission")
@RequiredArgsConstructor
@FuncPermission("权限管理")
public class PermissionController {

    private final PermissionDomain permissionDomain;
    private final PermissionSyncService permissionSyncService;

    /**
     * 按类型查询权限树
     *
     * @param type 权限类型
     * @return 权限树
     */
    @GetMapping("/tree")
    @FuncPermission("权限树")
    public PlatformResult<List<PermissionTreeVO>> tree(@RequestParam PermissionEnum.Type type) {
        return PlatformResult.success(permissionDomain.tree(type));
    }

    /**
     * 权限详情
     *
     * @param id 权限ID
     * @return 权限视图
     */
    @GetMapping("/detail")
    @FuncPermission("权限详情")
    public PlatformResult<PermissionVO> detail(@RequestParam Long id) {
        return PlatformResult.success(permissionDomain.detail(id));
    }

    /**
     * 创建菜单权限
     *
     * @param req 权限入参
     * @return 主键ID
     */
    @PostMapping("/create")
    @FuncPermission("创建权限")
    public PlatformResult<Long> create(@RequestBody @Valid PermissionReq req) {
        return PlatformResult.success(permissionDomain.create(req));
    }

    /**
     * 更新权限
     *
     * @param req 权限入参
     * @return 空结果
     */
    @PostMapping("/update")
    @FuncPermission("更新权限")
    public PlatformResult<Object> update(@RequestBody @Valid PermissionReq req) {
        permissionDomain.update(req);
        return PlatformResult.success();
    }

    /**
     * 逻辑删除菜单权限
     *
     * @param id 权限ID
     * @return 空结果
     */
    @PostMapping("/delete")
    @FuncPermission("删除权限")
    public PlatformResult<Object> delete(@RequestParam Long id) {
        permissionDomain.delete(id);
        return PlatformResult.success();
    }

    /**
     * 批量导入菜单
     *
     * @param menus 菜单树
     * @return 处理节点数
     */
    @PostMapping("/importMenu")
    @FuncPermission("导入菜单")
    public PlatformResult<Integer> importMenu(@RequestBody PermissionListDTO menus) {
        return PlatformResult.success(permissionDomain.importMenu(menus));
    }

    /**
     * 扫包同步功能权限
     *
     * @return 同步结果统计
     */
    @PostMapping("/syncFunc")
    @FuncPermission("同步功能权限")
    public PlatformResult<PermissionDomain.SyncResult> syncFunc() {
        return PlatformResult.success(permissionSyncService.sync());
    }

    /**
     * 按权限反查并重算受影响账号
     *
     * @param id 权限ID
     * @return 受影响账号数
     */
    @PostMapping("/syncAccounts")
    @FuncPermission("重算权限账号")
    public PlatformResult<Integer> syncAccounts(@RequestParam Long id) {
        return PlatformResult.success(permissionDomain.syncAccountsByPermission(id));
    }
}
