package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.auth.service.AuthDomain;
import com.newzkl.platform.base.biz.account.model.auth.req.FunctionPageQuery;
import com.newzkl.platform.base.biz.account.model.auth.req.FunctionRelationsReq;
import com.newzkl.platform.base.biz.account.model.auth.req.FunctionReq;
import com.newzkl.platform.base.biz.account.model.auth.req.FunctionTreeQuery;
import com.newzkl.platform.base.biz.account.model.auth.req.MenuReq;
import com.newzkl.platform.base.biz.account.model.auth.req.RolePageQuery;
import com.newzkl.platform.base.biz.account.model.auth.req.RoleReq;
import com.newzkl.platform.base.biz.account.model.auth.vo.FunctionVO;
import com.newzkl.platform.base.biz.account.model.auth.vo.LimitRoleVO;
import com.newzkl.platform.base.biz.account.model.auth.vo.MenuTreeVO;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
    public ScmResult<Void> roleCreate(@Validated @RequestBody RoleReq roleReq) {
        authDomain.roleCreate(roleReq);
        return ScmResult.success();
    }

    /**
     * 删除角色。
     *
     * @param roleId 角色 ID
     * @return 成功结果
     */
    @PostMapping("roleDelete")
    public ScmResult<Void> roleDelete(@RequestParam("roleId") Long roleId) {
        authDomain.roleDelete(roleId);
        return ScmResult.success();
    }

    /**
     * 编辑角色。
     *
     * @param roleReq 角色请求
     * @return 成功结果
     */
    @PostMapping("roleEdit")
    public ScmResult<Void> roleEdit(@Validated @RequestBody RoleReq roleReq) {
        authDomain.roleEdit(roleReq);
        return ScmResult.success();
    }

    /**
     * 角色分页。
     *
     * @param rolePageQuery 角色分页查询
     * @return 角色分页
     */
    @PostMapping("rolePage")
    public ScmResult<Page<LimitRoleVO>> rolePage(@RequestBody RolePageQuery rolePageQuery) {
        return ScmResult.success(authDomain.rolePage(rolePageQuery));
    }

    /**
     * 创建功能点。
     *
     * @param req 功能点请求
     * @return 成功结果
     */
    @PostMapping("createFunction")
    public ScmResult<Void> createFunction(@Validated @RequestBody FunctionReq req) {
        authDomain.createFunction(req);
        return ScmResult.success();
    }

    /**
     * 更新功能点。
     *
     * @param req 功能点请求
     * @return 成功结果
     */
    @PostMapping("updateFunction")
    public ScmResult<Void> updateFunction(@Validated @RequestBody FunctionReq req) {
        authDomain.updateFunction(req);
        return ScmResult.success();
    }

    /**
     * 删除功能点。
     *
     * @param id 功能点 ID
     * @return 成功结果
     */
    @PostMapping("deleteFunction")
    public ScmResult<Void> deleteFunction(@RequestParam("id") Long id) {
        authDomain.deleteFunction(id);
        return ScmResult.success();
    }

    /**
     * 功能点分页。
     *
     * @param query 功能点分页查询
     * @return 功能点分页
     */
    @PostMapping("functionPage")
    public ScmResult<Page<FunctionVO>> functionPage(@RequestBody FunctionPageQuery query) {
        return ScmResult.success(authDomain.functionPage(query));
    }

    /**
     * 功能点树形结构。
     *
     * @param functionTreeQuery 功能点树查询
     * @return 菜单树列表
     */
    @PostMapping("functionTreeList")
    public ScmResult<List<MenuTreeVO>> functionTreeList(@RequestBody FunctionTreeQuery functionTreeQuery) {
        return ScmResult.success(authDomain.functionTreeList(functionTreeQuery));
    }

    /**
     * 未录入接口列表。
     *
     * @return 功能点列表
     */
    @PostMapping("noEnteredFunctionList")
    public ScmResult<List<FunctionVO>> noEnteredFunctionList() {
        return ScmResult.success(authDomain.noEnteredFunctionList());
    }

    /**
     * 废弃接口列表。
     *
     * @return 功能点列表
     */
    @PostMapping("deprecatedFunctionList")
    public ScmResult<List<FunctionVO>> deprecatedFunctionList() {
        return ScmResult.success(authDomain.deprecatedFunctionList());
    }

    /**
     * 创建菜单。
     *
     * @param req 菜单请求
     * @return 成功结果
     */
    @PostMapping("createMenu")
    public ScmResult<Void> createMenu(@Validated @RequestBody MenuReq req) {
        authDomain.createMenu(req);
        return ScmResult.success();
    }

    /**
     * 更新菜单。
     *
     * @param req 菜单请求
     * @return 成功结果
     */
    @PostMapping("updateMenu")
    public ScmResult<Void> updateMenu(@Validated @RequestBody MenuReq req) {
        authDomain.updateMenu(req);
        return ScmResult.success();
    }

    /**
     * 删除菜单。
     *
     * @param id 菜单 ID
     * @return 成功结果
     */
    @PostMapping("deleteMenu")
    public ScmResult<Void> deleteMenu(@RequestParam("id") Long id) {
        authDomain.deleteMenu(id);
        return ScmResult.success();
    }

    /**
     * 批量勾选权限。
     *
     * @param req 功能点关联请求
     * @return 成功结果
     */
    @PostMapping("checkFunction")
    public ScmResult<Void> checkFunction(@RequestBody FunctionRelationsReq req) {
        authDomain.checkFunction(req);
        return ScmResult.success();
    }

    /**
     * 取消勾选权限。
     *
     * @param req 功能点关联请求
     * @return 成功结果
     */
    @PostMapping("cancelCheckFunction")
    public ScmResult<Void> cancelCheckFunction(@RequestBody FunctionRelationsReq req) {
        authDomain.cancelCheckFunction(req);
        return ScmResult.success();
    }

    /**
     * 角色功能点信息列表。
     *
     * @param roleId 角色 ID
     * @return 菜单树列表
     */
    @PostMapping("roleFunctionTreeList")
    public ScmResult<List<MenuTreeVO>> roleFunctionTreeList(@RequestParam("roleId") Long roleId) {
        return ScmResult.success(authDomain.roleFunctionTreeList(roleId));
    }

    /**
     * 菜单关联权限。
     *
     * @param functionId 功能点 ID
     * @param menuId     菜单 ID
     * @return 成功结果
     */
    @PostMapping("menuFunctionRelations")
    public ScmResult<Void> menuFunctionRelations(@RequestParam("functionId") Long functionId,
                                                 @RequestParam("menuId") Long menuId) {
        authDomain.menuFunctionRelations(functionId, menuId);
        return ScmResult.success();
    }

    /**
     * 菜单取消权限。
     *
     * @param functionId 功能点 ID
     * @param menuId     菜单 ID
     * @return 成功结果
     */
    @PostMapping("menuCancelFunction")
    public ScmResult<Void> menuCancelFunction(@RequestParam("functionId") Long functionId,
                                              @RequestParam("menuId") Long menuId) {
        authDomain.menuCancelFunction(functionId, menuId);
        return ScmResult.success();
    }
}
