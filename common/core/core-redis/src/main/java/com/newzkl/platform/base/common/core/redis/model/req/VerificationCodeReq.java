package com.newzkl.platform.base.common.core.redis.model.req;


import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 校验验证码请求参数
 */
@Data
public class VerificationCodeReq implements Serializable {
    /**
     * 手机号
     */
    @NotNull
    private String phone;

    /**
     * 类型
     */
    @NotBlank
    private SmsEnum.Type type;

    /**
     * 验证码
     */
    @NotBlank
    private String code;
}
