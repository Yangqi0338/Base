package com.newzkl.platform.base.biz.account.model.support;

import com.newzkl.platform.base.biz.account.model.enums.SmsEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CodeReq {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "类型不能为空")
    private SmsEnum.Type type;

    private List<String> params;

    private String templateId;

}
