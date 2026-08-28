package com.newzkl.platform.base.biz.auth.action.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.nacos.api.model.v2.Result;
import com.newzkl.platform.base.biz.auth.action.service.PermissionSyncService;
import com.newzkl.platform.base.biz.auth.domain.service.PermissionDomain;
import com.newzkl.platform.base.biz.auth.domain.service.RelationDomain;
import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.auth.model.permission.req.PermissionQuery;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.permission.vo.*;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionListDTO;
import com.newzkl.platform.base.biz.auth.model.permission.req.PermissionReq;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FuncPermission("权限管理")
public class PermissionController {

    private final PermissionDomain permissionDomain;
    private final RoleDomain roleDomain;
    private final RelationDomain relationDomain;
    private final PermissionSyncService permissionSyncService;

    /**
     * 按类型查询权限树
     *
     * @param type 权限类型
     * @return 权限树
     */
    @GetMapping("/tree")
    public PlatformResult<List<PermissionTreeVO>> tree(@RequestParam PermissionEnum.Type type) {
        return PlatformResult.success(permissionDomain.tree(SecurityUtils.getClient(), type));
    }

    /**
     * 权限详情
     *
     * @param id 权限ID
     * @return 权限视图
     */
    @GetMapping("/detail")
    public PlatformResult<PermissionVO> detail(@RequestParam Long id) {
        return PlatformResult.success(permissionDomain.detail(id));
    }

    /**
     * 保存菜单权限(新增/修改)
     *
     * <p>合并旧 {@code /create} 与 {@code /update}: 入参同为 {@link PermissionReq}(含 id),
     * 按 id 有无分流 —— 空则 {@code create} 返回新 id, 非空则 {@code update} 返回入参 id。
     * 原「创建权限」「更新权限」两功能权限点合并为「保存权限」, 权限粒度变粗。</p>
     *
     * @param req 权限入参(id 为空新增, 非空修改)
     * @return 主键ID
     */
    @PostMapping("/save")
    @FuncPermission("保存权限")
    public PlatformResult<Long> save(@RequestBody @Valid PermissionReq req) {
        Long id = req.getId();
        if (id == null) {
            return PlatformResult.success(permissionDomain.create(req));
        }
        permissionDomain.update(req);
        return PlatformResult.success(id);
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

    /**
     * 个人权限树
     * @return 权限树
     */
    @GetMapping("/mine")
    public PlatformResult<MinePermissionVO> mine() {

        AccountEnum.Client client = SecurityUtils.getClient();
        List<String> permissions = StpUtil.getPermissionList();
        List<String> roles = StpUtil.getRoleList();

        MinePermissionVO vo = new MinePermissionVO();

        // TODO 缓存优化
        List<PermissionTreeVO> tree = permissionDomain.tree(client, PermissionEnum.Type.MENU);
        if (!CollUtil.contains(permissions,"*")) {
            PermissionQuery query = new PermissionQuery();
            query.resetQueryList();
            query.setIdList(CollUtil.map(permissions, NumberUtil::parseLong, true));
            Set<Long> menuPermIds = permissionDomain.page(query).getRecords().stream()
                    .map(PermissionVO::getId).collect(Collectors.toSet());
            vo.setMenus(filterTree(tree, menuPermIds));
        }

        RoleQuery roleQuery = new RoleQuery();
        roleQuery.resetQueryList();
        roleQuery.setIdList(CollUtil.map(roles, NumberUtil::parseLong, true));
        List<RoleRes> records = roleDomain.page(roleQuery).getRecords();
        vo.setRoles(TransferUtils.transfers(records, RoleVO.class));
        return PlatformResult.success(vo);
    }

    private List<PermissionTreeVO> filterTree(List<PermissionTreeVO> tree, Set<Long> allowedIds) {
        List<PermissionTreeVO> result = new ArrayList<>();
        if (tree == null) return result;
        for (PermissionTreeVO node : tree) {
            List<PermissionTreeVO> filteredChildren = filterTree(node.getChildren(), allowedIds);
            if (allowedIds.contains(node.getId()) || !filteredChildren.isEmpty()) {
                node.setChildren(filteredChildren);
                result.add(node);
            }
        }
        return result;
    }
}
