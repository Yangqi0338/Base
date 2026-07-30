package com.newzkl.platform.base.common.core.sms;

import lombok.Data;

import java.util.List;

/**
 * 短信发送入参 (技术层)
 *
 * <p>本模块只关心「发给谁 / 用哪个模板 / 填什么变量」, 不承载任何业务字段。
 * 业务侧 (如验证码) 自带 Req, 调用前转成本对象。</p>
 *
 * @author KC
 */
@Data
public class SmsSendReq {

    /**
     * 接收手机号
     */
    private String phone;

    /**
     * 三方短信模板号
     */
    private String templateId;

    /**
     * 模板变量, 按模板占位顺序排列
     */
    private List<String> params;

    /**
     * 按手机号 + 模板号构造发送入参
     *
     * @param phone      接收手机号
     * @param templateId 三方短信模板号
     * @param params     模板变量, 可为 {@code null}
     * @return 发送入参
     */
    public static SmsSendReq of(String phone, String templateId, List<String> params) {
        SmsSendReq req = new SmsSendReq();
        req.setPhone(phone);
        req.setTemplateId(templateId);
        req.setParams(params);
        return req;
    }
}
