package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/2711:45
 */
@Data
public class AuditAccountVO implements Serializable {
    private Long accountId;
    private RoleEnum.CompanyRole role;
    private String username;
    private String name;
}
