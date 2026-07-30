package com.newzkl.platform.base.biz.sys.application.service;

import com.newzkl.platform.base.biz.sys.model.code.req.CodeReq;

/**
 * 短信验证码编排接口
 *
 * <p>迁移自 new-scm {@code message.application.service.ICodeService}。
 * 旧接口的 {@code sendCode} (华为云通道) 未迁移: Base 无华为短信设施, 且被迁控制器不调用。</p>
 *
 * <p>通道细节与商户凭证在 {@code core-sms}, 本层只做业务编排。</p>
 *
 * @author KC
 */
public interface CodeService {

    /**
     * 经连连通道发送短信
     *
     * @param codeReq 短信入参 (需已填好 {@code templateId})
     * @return 三方受理成功返回 {@code true}
     */
    boolean sendCodeLianLu(CodeReq codeReq);

    /**
     * 发送通知类短信 (按类型自动取模板号)
     *
     * @param codeReq 短信入参
     */
    void sendNotifyCode(CodeReq codeReq);
}
