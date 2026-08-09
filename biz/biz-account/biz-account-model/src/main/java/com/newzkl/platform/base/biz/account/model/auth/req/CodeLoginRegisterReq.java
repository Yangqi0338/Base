package com.newzkl.platform.base.biz.account.model.auth.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 验证码登录并注册请求参数
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class CodeLoginRegisterReq {
    /**
     * 账号
     */
    @NotEmpty(message = "username")
    private String username;
    /**
     * 验证码
     */
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
     * 邀请码
     * @ext 邀请码和父账号二选一, 且非必填
     */
    private String yqm;

    /**
     * 父账号
     * @ext 邀请码和父账号二选一, 且非必填
     */
    private String superiorAccount;

    /**
     * 密码
     */
    private String password;
}
