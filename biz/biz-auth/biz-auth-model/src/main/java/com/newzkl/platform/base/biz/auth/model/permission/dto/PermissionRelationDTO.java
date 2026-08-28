package com.newzkl.platform.base.biz.auth.model.permission.dto;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import lombok.Data;

/**
 * 权限关系数据传输对象
 *
 * @author KC
 */
@Data
public class PermissionRelationDTO {

    /** 主键ID */
    private Long id;

    /** 所属端 */
    private AccountEnum.Client client;

    /** 关系类型 */
    private PermissionEnum.RelationType type;

    /**
     * 源对象标识
     * @ext account 侧存账号 id 字符串, role 侧存角色 code
     */
    private String source;

    /**
     * 目标对象标识
     * @ext role 侧存角色 code, permission 侧存权限 id 字符串
     */
    private String target;

    /** 关系来源 */
    private PermissionEnum.Source origin;
}
