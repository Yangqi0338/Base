package com.newzkl.platform.base.biz.auth.model.permission.vo;

import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限视图
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionVO extends BaseRes {

    /** 父权限ID */
    private Long pid;

    /** 权限类型 */
    private PermissionEnum.Type type;

    /** 权限编码 */
    private String code;

    /** 权限名称 */
    private String name;

    /** 前端路由 */
    private String route;

    /** 菜单图标 */
    private String icon;

    /** 排序 */
    private Integer sort;
}
