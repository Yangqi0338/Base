package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/2711:45
 */
@Data
public class AuditAccountVO implements Serializable {
    /** 账户ID */
    private Long accountId;
    /** 企业角色 */
    private RoleEnum.CompanyRole role;
    /** 账号 */
    private String username;
    /** 名称 */
    private String name;
}
