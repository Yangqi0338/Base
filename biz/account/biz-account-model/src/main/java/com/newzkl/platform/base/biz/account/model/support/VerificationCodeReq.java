package com.newzkl.platform.base.biz.account.model.support;

import com.newzkl.platform.base.biz.account.model.enums.SmsEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class VerificationCodeReq implements Serializable {
    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;

    /**
     * 类型
     */
    @NotBlank(message = "验证类型不能为空")
    private SmsEnum.Type type;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
}
