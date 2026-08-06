package com.newzkl.platform.base.biz.auth.model.role.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色增改入参
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleReq extends BaseReq {

    /** 角色编码 */
    private String code;

    /** 角色名称 */
    private String name;

    /** 角色描述 */
    private String description;

    /** 排序 */
    private Integer sort;
}
