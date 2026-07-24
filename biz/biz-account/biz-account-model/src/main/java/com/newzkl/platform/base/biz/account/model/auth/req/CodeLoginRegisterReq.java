package com.newzkl.platform.base.biz.account.model.auth.req;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:22
 */
@Data
public class CodeLoginRegisterReq {
    @NotEmpty(message = "username")
    private String username;
    @NotEmpty(message = "code?")
    private String code;
    /**
     * 指定角色
     */
    private RoleEnum.CompanyRole role;

    /**
     * 端
     */
    private CommonEnum.Client client;

    /**
     * 邀请码(邀请码和父账号 二选一 且非必填)
     */
    private String yqm;

    /**
     * 父账号(邀请码和父账号 二选一 且非必填)
     */
    private String superiorAccount;

    /**
     * 密码
     */
    private String password;
}
