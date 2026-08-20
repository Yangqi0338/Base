package com.newzkl.platform.base.common.core.sms;

import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 短信验证码发送请求
 */
@Data
public class CodeReq {

    /**
     * 手机号
     */
    @NotBlank
    private String phone;

    /**
     * 类型
     */
    @NotNull
    private SmsEnum.Type type;

    /**
     * 模板变量参数列表
     */
    private List<String> params;

    /**
     * 短信模板ID
     */
    private String templateId;

}
