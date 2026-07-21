package com.newzkl.platform.base.biz.account.domain.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.auth.req.*;
import com.newzkl.platform.base.biz.account.model.auth.vo.FunctionVO;
import com.newzkl.platform.base.biz.account.model.auth.vo.LimitRoleVO;
import com.newzkl.platform.base.biz.account.model.auth.vo.MenuTreeVO;

import java.util.List;

public interface AuthDomain {

    void roleCreate(RoleReq roleReq);

    void roleDelete(Long roleId);

    void roleEdit(RoleReq roleReq);

    Page<LimitRoleVO> rolePage(RolePageQuery rolePageQuery);

    void updateFunction(FunctionReq req);

    void createFunction(FunctionReq req);

    void deleteFunction(Long id);

    Page<FunctionVO> functionPage(FunctionPageQuery query);

    /**
     * 获取功能点树形结构，并标识指定角色已拥有的功能点
     */
    List<MenuTreeVO> functionTreeList(FunctionTreeQuery functionTreeQuery);

    /**
     * 未录入接口列表
     */
    List<FunctionVO> noEnteredFunctionList();

    /**
     * 废弃接口列表
     */
    List<FunctionVO> deprecatedFunctionList();

    void createMenu(MenuReq req);

    void updateMenu(MenuReq req);

    void deleteMenu(Long id);

    /**
     * 批量勾选权限
     */
    void checkFunction(FunctionRelationsReq req);

    /**
     * 取消勾选权限
     */
    void cancelCheckFunction(FunctionRelationsReq req);

    /**
     * 角色功能点信息列表
     */
    List<MenuTreeVO> roleFunctionTreeList(Long roleId);

    /**
     * 菜单关联权限
     */
    void menuFunctionRelations(Long functionId, Long menuId);

    /**
     * 菜单取消权限
     */
    void menuCancelFunction(Long functionId, Long menuId);
}
