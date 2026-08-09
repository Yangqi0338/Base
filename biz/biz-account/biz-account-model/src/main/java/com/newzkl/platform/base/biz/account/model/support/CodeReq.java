package com.newzkl.platform.base.biz.account.model.support;

import com.newzkl.platform.base.common.core.sms.enums.SmsEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 发送短信验证码请求参数
 */
@Data
public class CodeReq {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /** 短信类型 */
    @NotBlank(message = "类型不能为空")
    private SmsEnum.Type type;

    /** 短信模板参数 */
    private List<String> params;

    /** 短信模板ID */
    private String templateId;

}
