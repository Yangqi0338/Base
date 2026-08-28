package com.newzkl.platform.base.biz.auth.model.permission.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色视图
 *
 * @author KC
 */
@Data
public class RoleVO {

    /** 角色编码 */
    private String code;

    /** 角色名称 */
    private String name;

    /** 角色描述 */
    private String description;

}
