package com.newzkl.platform.base.biz.auth.model.oauth.req;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.AuthEnum;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 登录请求参数
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class LoginReq {
    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 注册角色
     */
    private AccountEnum.Identity identity;

    /**
     * 登录类型
     */
    @NotNull(message = "登录类型不能为空")
    private AuthEnum.Type type;

    /**
     * 微信小程序登录 code
     *
     * <p>小程序端登录传入, 后端经 jscode2session 换 openId/unionId 并按身份回写;
     * PC 端不传, 不触发微信绑定。</p>
     */
    private String wxCode;

    /**
     * 校验密码登录时密码是否为空
     *
     * @return 密码为空时返回 true
     */
    @AssertFalse(message = "密码不能为空")
    public boolean isPasswordBlank() {
        return type == AuthEnum.Type.PASSWORD && StrUtil.isBlank(password);
    }

    /**
     * 校验验证码登录时验证码是否为空
     *
     * @return 验证码为空时返回 true
     */
    @AssertFalse(message = "验证码不能为空")
    public boolean isCodeBlank() {
        return type == AuthEnum.Type.CODE && StrUtil.isBlank(code);
    }

}
