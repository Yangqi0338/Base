package com.newzkl.platform.base.biz.auth.model.permission.vo;

import lombok.Data;

import java.util.List;

/**
 * 当前用户权限聚合
 */
@Data
public class MinePermissionVO {
    /** 菜单权限树 */
    private List<PermissionTreeVO> menus;
    /**
     * 功能权限 code 列表
     */
    private List<String> funcs;
    /** 角色列表 */
    private List<RoleVO> roles;
}
