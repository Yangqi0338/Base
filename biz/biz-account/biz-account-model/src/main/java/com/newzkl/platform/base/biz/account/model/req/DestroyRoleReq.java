package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 注销角色请求参数
 *
 * @author muc_fang
 * @date 2024/2/22 18:13
 */
@Data
public class DestroyRoleReq {
    /**
     * 注销目标账号 id
     */
    private Long id;
    /**
     * 角色
     */
    @NotNull
    private AccountEnum.Identity identity;
    /**
     * 验证码
     */
    private String code;
    /**
     * 注销原因
     */
    @NotEmpty
    private String destroyReason;
}
