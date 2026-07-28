package com.newzkl.platform.base.biz.auth.model.rbac.req;

import lombok.Data;

/**
 * 功能点树形结构查询
 */
@Data
public class FunctionTreeQuery {

    /**
     * 系统id
     */
    private Long systemId;

    /**
     * 角色id
     */
    private Long roleId;
}
