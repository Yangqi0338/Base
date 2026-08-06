package com.newzkl.platform.base.biz.auth.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.domain.service.AuthDomain;
import com.newzkl.platform.base.biz.auth.model.enums.AuthEnum;
import com.newzkl.platform.base.biz.auth.model.rbac.req.FunctionPageQuery;
import com.newzkl.platform.base.biz.auth.model.rbac.req.FunctionRelationsReq;
import com.newzkl.platform.base.biz.auth.model.rbac.req.FunctionReq;
import com.newzkl.platform.base.biz.auth.model.rbac.req.FunctionTreeQuery;
import com.newzkl.platform.base.biz.auth.model.rbac.req.MenuReq;
import com.newzkl.platform.base.biz.auth.model.rbac.req.RolePageQuery;
import com.newzkl.platform.base.biz.auth.model.rbac.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.FunctionVO;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.LimitRoleVO;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.MenuTreeVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户-权限控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.AuthController}。
 * 类级路径与每个方法的路径 / HTTP 动词 / 参数注解形态逐字沿用旧实现,
 * 其中 {@code roleDelete} / {@code deleteMenu} / {@code deleteFunction} /
 * {@code roleFunctionTreeList} / {@code noEnteredFunctionList} /
 * {@code deprecatedFunctionList} / {@code systemList} /
 * {@code menuRelevancyFunction} / {@code menuCancelFunction} 为
 * {@code GET + @RequestParam}, 不得按 REST 习惯改写为 POST。</p>
 *
 * <p>出参壳由旧 {@code ScmResult} 换为 {@code PlatformResult}, 分页容器沿用
 * MyBatis-Plus {@code Page} (旧实现已是 Page, 非 PageInfo)。Base 不迁鉴权注解,
 * 鉴权统一由入口 (网关 / 鉴权基建) 承担。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthDomain authDomain;

    /**
     * 新增角色
     *
     * @param roleReq 角色入参
     * @return 空返回
     */
    @PostMapping("roleCreate")
    public PlatformResult<Void> roleCreate(@Validated @RequestBody RoleReq roleReq) {
        authDomain.roleCreate(roleReq);
        return PlatformResult.success();
    }

    /**
     * 角色删除
     *
     * @param roleId 角色 ID
     * @return 空返回
     */
    @GetMapping("roleDelete")
    public PlatformResult<Void> roleDelete(@RequestParam Long roleId) {
        authDomain.roleDelete(roleId);
        return PlatformResult.success();
    }

    /**
     * 角色修改
     *
     * @param roleReq 角色入参
     * @return 空返回
     */
    @PostMapping("roleEdit")
    public PlatformResult<Void> roleEdit(@RequestBody RoleReq roleReq) {
        if (roleReq.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "缺少ID");
        }
        roleReq.setMenderId(SecurityUtils.getAccountId());
        authDomain.roleEdit(roleReq);
        return PlatformResult.success();
    }

    /**
     * 角色分页
     *
     * @param rolePageQuery 角色分页查询
     * @return 角色分页
     */
    @PostMapping("rolePage")
    public PlatformResult<Page<LimitRoleVO>> rolePage(@RequestBody RolePageQuery rolePageQuery) {
        return PlatformResult.success(authDomain.rolePage(rolePageQuery));
    }

    /**
     * 新增菜单
     *
     * @param req 菜单入参
     * @return 空返回
     */
    @PostMapping("createMenu")
    public PlatformResult<Void> createMenu(@RequestBody MenuReq req) {
        req.setCreateId(SecurityUtils.getAccountId());
        authDomain.createMenu(req);
        return PlatformResult.success();
    }

    /**
     * 修改菜单
     *
     * @param req 菜单入参
     * @return 空返回
     */
    @PostMapping("updateMenu")
    public PlatformResult<Void> updateMenu(@RequestBody MenuReq req) {
        req.setMenderId(SecurityUtils.getAccountId());
        authDomain.updateMenu(req);
        return PlatformResult.success();
    }

    /**
     * 删除菜单
     *
     * @param id 菜单 ID
     * @return 空返回
     */
    @GetMapping("deleteMenu")
    public PlatformResult<Void> deleteMenu(@RequestParam Long id) {
        authDomain.deleteMenu(id);
        return PlatformResult.success();
    }

    /**
     * 新增权限
     *
     * @param req 权限入参
     * @return 空返回
     */
    @PostMapping("createFunction")
    public PlatformResult<Void> createFunction(@RequestBody FunctionReq req) {
        req.setCreateId(SecurityUtils.getAccountId());
        authDomain.createFunction(req);
        return PlatformResult.success();
    }

    /**
     * 修改权限
     *
     * @param req 权限入参
     * @return 空返回
     */
    @PostMapping("updateFunction")
    public PlatformResult<Void> updateFunction(@RequestBody FunctionReq req) {
        req.setMenderId(SecurityUtils.getAccountId());
        authDomain.updateFunction(req);
        return PlatformResult.success();
    }

    /**
     * 删除权限
     *
     * @param id 权限 ID
     * @return 空返回
     */
    @GetMapping("deleteFunction")
    public PlatformResult<Void> deleteFunction(@RequestParam Long id) {
        authDomain.deleteFunction(id);
        return PlatformResult.success();
    }

    /**
     * 功能点列表
     *
     * @param query 功能点分页查询
     * @return 功能点分页
     */
    @PostMapping("functionPage")
    public PlatformResult<Page<FunctionVO>> functionPage(@RequestBody FunctionPageQuery query) {
        return PlatformResult.success(authDomain.functionPage(query));
    }

    /**
     * 功能点信息列表 (全量功能点)
     *
     * @param functionTreeQuery 功能点树查询
     * @return 菜单功能点树
     */
    @PostMapping("functionTreeList")
    public PlatformResult<List<MenuTreeVO>> functionTreeList(@RequestBody FunctionTreeQuery functionTreeQuery) {
        return PlatformResult.success(authDomain.functionTreeList(functionTreeQuery));
    }

    /**
     * 角色功能点信息列表
     *
     * @param roleId 角色 ID
     * @return 菜单功能点树
     */
    @GetMapping("roleFunctionTreeList")
    public PlatformResult<List<MenuTreeVO>> roleFunctionTreeList(@RequestParam Long roleId) {
        return PlatformResult.success(authDomain.roleFunctionTreeList(roleId));
    }

    /**
     * 未录入接口列表
     *
     * @return 功能点列表
     */
    @GetMapping("noEnteredFunctionList")
    public PlatformResult<List<FunctionVO>> noEnteredFunctionList() {
        return PlatformResult.success(authDomain.noEnteredFunctionList());
    }

    /**
     * 废弃接口列表
     *
     * @return 功能点列表
     */
    @GetMapping("deprecatedFunctionList")
    public PlatformResult<List<FunctionVO>> deprecatedFunctionList() {
        return PlatformResult.success(authDomain.deprecatedFunctionList());
    }

    /**
     * 系统列表
     *
     * <p>方法名沿用旧实现 {@code serviceList}, 路径为 {@code systemList}。</p>
     *
     * @return 系统枚举名到中文名的映射
     */
    @GetMapping("systemList")
    public PlatformResult<Map<String, String>> serviceList() {
        Map<String, String> map = Arrays.stream(AuthEnum.SystemType.values())
                .collect(Collectors.toMap(Enum::name, AuthEnum.SystemType::getValue, (v1, v2) -> v1));
        return PlatformResult.success(map);
    }

    /**
     * 菜单关联权限
     *
     * @param functionId 权限 ID
     * @param menuId     菜单 ID
     * @return 空返回
     */
    @GetMapping("menuRelevancyFunction")
    public PlatformResult<Void> menuFunctionRelations(@RequestParam Long functionId, @RequestParam Long menuId) {
        authDomain.menuFunctionRelations(functionId, menuId);
        return PlatformResult.success();
    }

    /**
     * 菜单取消权限
     *
     * @param functionId 权限 ID
     * @param menuId     菜单 ID
     * @return 空返回
     */
    @GetMapping("menuCancelFunction")
    public PlatformResult<Void> menuCancelFunction(@RequestParam Long functionId, @RequestParam Long menuId) {
        authDomain.menuCancelFunction(functionId, menuId);
        return PlatformResult.success();
    }

    /**
     * 勾选权限
     *
     * @param req 权限关系入参
     * @return 空返回
     */
    @PostMapping("checkFunction")
    public PlatformResult<Void> checkFunction(@RequestBody FunctionRelationsReq req) {
        authDomain.checkFunction(req);
        return PlatformResult.success();
    }

    /**
     * 取消勾选权限
     *
     * @param req 权限关系入参
     * @return 空返回
     */
    @PostMapping("cancelCheckFunction")
    public PlatformResult<Void> cancelCheckFunction(@RequestBody FunctionRelationsReq req) {
        authDomain.cancelCheckFunction(req);
        return PlatformResult.success();
    }

}
