package com.newzkl.platform.base.biz.auth.model.permission.dto;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 权限数据传输对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionDTO extends BaseDTO {

    /** 所属端 */
    private AccountEnum.Client client;

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

    /**
     * 子节点
     * @ext 仅供导入嵌套结构使用, 不存库
     */
    private List<PermissionDTO> children;
}
