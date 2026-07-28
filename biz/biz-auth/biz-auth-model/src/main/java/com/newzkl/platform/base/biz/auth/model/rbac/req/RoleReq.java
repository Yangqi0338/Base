package com.newzkl.platform.base.biz.auth.model.rbac.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleReq {

    /**
     * id
     */
    private Long id;

    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空")
    private String name;

    /**
     * 描述
     */
    private String des;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改人id
     */
    private Long menderId;

    /**
     * 系统id
     */
    private Long systemId;

}
