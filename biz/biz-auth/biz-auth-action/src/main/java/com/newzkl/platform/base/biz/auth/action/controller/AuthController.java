package com.newzkl.platform.base.biz.auth.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.domain.service.AuthDomain;
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
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限控制器 (角色 / 功能点 / 菜单)。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthDomain authDomain;

    /**
     * 创建角色。
     *
     * @param roleReq 角色请求
     * @return 成功结果
     */
    @PostMapping("roleCreate")
    public PlatformResult<Void> roleCreate(@Validated @RequestBody RoleReq roleReq) {
        authDomain.roleCreate(roleReq);
        return PlatformResult.success();
    }

    /**
     * 删除角色。
     *
     * @param roleId 角色 ID
     * @return 成功结果
     */
    @GetMapping("roleDelete")
    public PlatformResult<Void> roleDelete(@RequestParam("roleId") Long roleId) {
        authDomain.roleDelete(roleId);
        return PlatformResult.success();
    }

    /**
     * 编辑角色。
     *
     * @param roleReq 角色请求
     * @return 成功结果
     */
    @PostMapping("roleEdit")
    public PlatformResult<Void> roleEdit(@Validated @RequestBody RoleReq roleReq) {
        authDomain.roleEdit(roleReq);
        return PlatformResult.success();
    }

    /**
     * 角色分页。
     *
     * @param rolePageQuery 角色分页查询
     * @return 角色分页
     */
    @PostMapping("rolePage")
    public PlatformResult<Page<LimitRoleVO>> rolePage(@RequestBody RolePageQuery rolePageQuery) {
        return PlatformResult.success(authDomain.rolePage(rolePageQuery));
    }

    /**
     * 创建功能点。
     *
     * @param req 功能点请求
     * @return 成功结果
     */
    @PostMapping("createFunction")
    public PlatformResult<Void> createFunction(@Validated @RequestBody FunctionReq req) {
        authDomain.createFunction(req);
        return PlatformResult.success();
    }

    /**
     * 更新功能点。
     *
     * @param req 功能点请求
     * @return 成功结果
     */
    @PostMapping("updateFunction")
    public PlatformResult<Void> updateFunction(@Validated @RequestBody FunctionReq req) {
        authDomain.updateFunction(req);
        return PlatformResult.success();
    }

    /**
     * 删除功能点。
     *
     * @param id 功能点 ID
     * @return 成功结果
     */
    @GetMapping("deleteFunction")
    public PlatformResult<Void> deleteFunction(@RequestParam("id") Long id) {
        authDomain.deleteFunction(id);
        return PlatformResult.success();
    }

    /**
     * 功能点分页。
     *
     * @param query 功能点分页查询
     * @return 功能点分页
     */
    @PostMapping("functionPage")
    public PlatformResult<Page<FunctionVO>> functionPage(@RequestBody FunctionPageQuery query) {
        return PlatformResult.success(authDomain.functionPage(query));
    }

    /**
     * 功能点树形结构。
     *
     * @param functionTreeQuery 功能点树查询
     * @return 菜单树列表
     */
    @PostMapping("functionTreeList")
    public PlatformResult<List<MenuTreeVO>> functionTreeList(@RequestBody FunctionTreeQuery functionTreeQuery) {
        return PlatformResult.success(authDomain.functionTreeList(functionTreeQuery));
    }

    /**
     * 未录入接口列表。
     *
     * @return 功能点列表
     */
    @GetMapping("noEnteredFunctionList")
    public PlatformResult<List<FunctionVO>> noEnteredFunctionList() {
        return PlatformResult.success(authDomain.noEnteredFunctionList());
    }

    /**
     * 废弃接口列表。
     *
     * @return 功能点列表
     */
    @GetMapping("deprecatedFunctionList")
    public PlatformResult<List<FunctionVO>> deprecatedFunctionList() {
        return PlatformResult.success(authDomain.deprecatedFunctionList());
    }

    /**
     * 创建菜单。
     *
     * @param req 菜单请求
     * @return 成功结果
     */
    @PostMapping("createMenu")
    public PlatformResult<Void> createMenu(@Validated @RequestBody MenuReq req) {
        authDomain.createMenu(req);
        return PlatformResult.success();
    }

    /**
     * 更新菜单。
     *
     * @param req 菜单请求
     * @return 成功结果
     */
    @PostMapping("updateMenu")
    public PlatformResult<Void> updateMenu(@Validated @RequestBody MenuReq req) {
        authDomain.updateMenu(req);
        return PlatformResult.success();
    }

    /**
     * 删除菜单。
     *
     * @param id 菜单 ID
     * @return 成功结果
     */
    @GetMapping("deleteMenu")
    public PlatformResult<Void> deleteMenu(@RequestParam("id") Long id) {
        authDomain.deleteMenu(id);
        return PlatformResult.success();
    }

    /**
     * 批量勾选权限。
     *
     * @param req 功能点关联请求
     * @return 成功结果
     */
    @PostMapping("checkFunction")
    public PlatformResult<Void> checkFunction(@RequestBody FunctionRelationsReq req) {
        authDomain.checkFunction(req);
        return PlatformResult.success();
    }

    /**
     * 取消勾选权限。
     *
     * @param req 功能点关联请求
     * @return 成功结果
     */
    @PostMapping("cancelCheckFunction")
    public PlatformResult<Void> cancelCheckFunction(@RequestBody FunctionRelationsReq req) {
        authDomain.cancelCheckFunction(req);
        return PlatformResult.success();
    }

    /**
     * 角色功能点信息列表。
     *
     * @param roleId 角色 ID
     * @return 菜单树列表
     */
    @GetMapping("roleFunctionTreeList")
    public PlatformResult<List<MenuTreeVO>> roleFunctionTreeList(@RequestParam("roleId") Long roleId) {
        return PlatformResult.success(authDomain.roleFunctionTreeList(roleId));
    }

    /**
     * 菜单关联权限。
     *
     * @param functionId 功能点 ID
     * @param menuId     菜单 ID
     * @return 成功结果
     */
    @PostMapping("menuFunctionRelations")
    public PlatformResult<Void> menuFunctionRelations(@RequestParam("functionId") Long functionId,
                                                 @RequestParam("menuId") Long menuId) {
        authDomain.menuFunctionRelations(functionId, menuId);
        return PlatformResult.success();
    }

    /**
     * 菜单取消权限。
     *
     * @param functionId 功能点 ID
     * @param menuId     菜单 ID
     * @return 成功结果
     */
    @GetMapping("menuCancelFunction")
    public PlatformResult<Void> menuCancelFunction(@RequestParam("functionId") Long functionId,
                                              @RequestParam("menuId") Long menuId) {
        authDomain.menuCancelFunction(functionId, menuId);
        return PlatformResult.success();
    }
}
