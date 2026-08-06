package com.newzkl.platform.base.biz.auth.model.role.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色视图
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO extends BaseRes {

    /** 角色编码 */
    private String code;

    /** 角色名称 */
    private String name;

    /** 角色描述 */
    private String description;

    /** 排序 */
    private Integer sort;

    /** 绑定账号数 */
    private Integer accountCount;

    /** 权限数 */
    private Integer permissionCount;

    /**
     * 绑定账号ID列表
     * @ext 详情查时填
     */
    private List<Long> accountIds;

    /**
     * 权限ID列表
     * @ext 详情查时填, 含半选
     */
    private List<Long> permissionIds;

    /**
     * 半选权限ID列表
     * @ext 详情查时填
     */
    private List<Long> halfCheckedPermissionIds;
}
