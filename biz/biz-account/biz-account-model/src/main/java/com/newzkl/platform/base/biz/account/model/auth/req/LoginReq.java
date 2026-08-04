package com.newzkl.platform.base.biz.account.model.auth.req;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:22
 */
@Data
public class LoginReq {

    /**
     * 主账号id
     */
    private Long mainAccountId;

    /**
     * 用户名 | 手机号
     */
    @NotBlank(message = "账号或手机号不能为空")
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户密码
     */
    private String code;

    /**
     * 注册角色
     */
    private RoleEnum.CompanyRole role;

    /**
     * 端
     */
    @NotNull(message = "登录端不能为空")
    private CommonEnum.Client client;

    /**
     * 登录类型
     */
    @NotNull(message = "登录类型不能为空")
    private AccountEnum.LoginType type;

    @AssertFalse(message = "密码不能为空")
    public boolean isPasswordBlank() {
        return type == AccountEnum.LoginType.PASSWORD && StrUtil.isBlank(password);
    }

    @AssertFalse(message = "验证码不能为空")
    public boolean isCodeBlank() {
        return type == AccountEnum.LoginType.CODE && StrUtil.isBlank(code);
    }

}
