package com.newzkl.platform.base.biz.auth.model.rbac.req;


import com.newzkl.platform.base.biz.auth.model.enums.AuthEnum;
import lombok.Data;

/**
 * 权限关系请求对象
 */
@Data
public class FunctionRelationsReq {

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 类型
     *
     * @see AuthEnum.RelationType
     */
    private String type;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 功能点ID
     */
    private Long functionId;

}
