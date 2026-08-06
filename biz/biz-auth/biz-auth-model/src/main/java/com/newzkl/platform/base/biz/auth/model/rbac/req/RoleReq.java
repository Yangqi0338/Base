package com.newzkl.platform.base.biz.auth.model.rbac.req;

import com.newzkl.platform.base.common.ddd.model.auth.OauthUserId;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleReq extends BaseReq {

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
     * 修改人id
     */
    @OauthUserId
    private Long menderId;

    /**
     * 系统id
     */
    private Long systemId;

}
