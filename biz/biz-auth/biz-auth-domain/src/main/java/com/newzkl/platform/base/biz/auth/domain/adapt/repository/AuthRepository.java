package com.newzkl.platform.base.biz.auth.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.rbac.req.*;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.FunctionVO;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.LimitRoleVO;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.MenuTreeVO;

import java.util.List;

public interface AuthRepository {
    void roleCreate(RoleReq roleReq);

    void roleEdit(RoleReq roleReq);

    /**
     * 角色删除
     *
     * @return
     */
    void roleDelete(Long roleId);

    /**
     * 角色page
     *
     * @return
     */
    Page<LimitRoleVO> rolePage(RolePageQuery query);

    void updateFunction(FunctionReq req);

    void deleteFunction(Long id);

    Page<FunctionVO> functionPage(FunctionPageQuery query);

    void createFunction(FunctionReq req);

    /**
     * 获取功能点树形结构，并标识指定角色已拥有的功能点
     */
    List<MenuTreeVO> functionTreeList(FunctionTreeQuery functionTreeQuery);

    /**
     * 查询项目中存在但数据库中未录入的功能点
     * @return 未录入的功能点列表
     */
    List<FunctionVO> noEnteredFunctionList();

    /**
     * 查询数据库中存在但项目中已废弃的功能点
     * @return 已废弃的功能点列表
     */
    List<FunctionVO> deprecatedFunctionList();

    /**
     * 缓存用户没有的功能点URL路径集合
     * @param accountId 用户ID
     * @return 功能点URL路径集合
     */
    void cacheUserUnFunctionUrls(Long accountId);

    void createMenu(MenuReq req);

    void updateMenu(MenuReq req);

    void deleteMenu(Long id);

    void checkFunction(FunctionRelationsReq req);

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
