package com.newzkl.platform.base.biz.account.model.req.cmd;

import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户账号查询入参
 */
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