package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2218:13
 */
@Data
public class DestroyRoleReq {
    /**
     * 角色
     */
    @NotNull(message = "角色?")
    private RoleEnum.CompanyRole role;
    /**
     * 验证码
     */
    @NotEmpty(message = "验证码?")
    private String code;
    /**
     * 注销原因
     */
    @NotEmpty(message = "注销原因?")
    private String destroyReason;
}
