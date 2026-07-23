package com.newzkl.platform.base.biz.account.model.req.cmd;

import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserAccountQuery {
    /**
     * 角色ID
     */
    @NotNull
    private RoleEnum.CompanyRole role;
    /**
     * 账号ID
     */
    @NotNull
    private Long accountId;
}