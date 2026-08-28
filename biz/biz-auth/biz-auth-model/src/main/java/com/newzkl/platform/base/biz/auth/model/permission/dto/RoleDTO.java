package com.newzkl.platform.base.biz.auth.model.permission.dto;

import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色数据传输对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDTO extends BaseDTO {

    /** 所属端 */
    private AccountEnum.Client client;

    /** 角色编码 */
    private String code;

    /** 角色名称 */
    private String name;

    /** 角色描述 */
    private String description;

    /** 排序 */
    private Integer sort;
}
