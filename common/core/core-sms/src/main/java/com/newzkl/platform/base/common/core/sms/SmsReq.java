package com.newzkl.platform.base.common.core.sms;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 短信请求封装类
 */
class SmsReq {

    /**
     * 联麓短信发送请求体
     */
    @Data
    static class LianLuSendMsgReq {

        /**
         * 短信模板ID
         */
        @JsonProperty("TemplateId")
        @NotBlank
        private String templateId;

        /**
         * 模板变量参数列表
         */
        @JsonProperty("TemplateParamSet")
        @NotEmpty
        private List<String> templateParamSet;

        /**
         * 接收短信的手机号列表
         */
        @JsonProperty("PhoneNumberSet")
        @NotBlank
        private List<String> phoneNumberSet;

        /**
         * 短信类型
         */
        @JsonProperty("Type")
        @NotBlank
        private Integer type;
    }

}