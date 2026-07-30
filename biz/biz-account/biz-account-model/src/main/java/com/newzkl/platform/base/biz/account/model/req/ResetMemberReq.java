package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 找回密码
 *
 * <p>字段名逐字沿用旧 {@code com.zkl.scm.user.domain.account.model.req.ResetMemberCommand}。
 * 实际业务参数全在 {@code sign} 的 AES 密文里 (解出 {@code ResetMemberVO}),
 * 其余明文字段旧代码也不读, 仅为兼容前端已有请求体形态而保留</p>
 *
 * @author sijiwang
 */
@Data
public class ResetMemberReq {

    /**
     * 密码
     */
    private String newPassword;

    /**
     * 发验证码的手机号（现在绑定的手机号）
     */
    private String phone;

    /**
     * 设备码
     */
    private String deviceCode;

    /**
     * 验证码
     */
    private String code;

    /**
     * 签名时间戳
     */
    private Long timestamp;

    /**
     * 前端生成的随机nonce
     */
    private String nonce;

    /**
     * 加密字符串
     */
    @NotBlank(message = "sign不能为空")
    private String sign;
}
