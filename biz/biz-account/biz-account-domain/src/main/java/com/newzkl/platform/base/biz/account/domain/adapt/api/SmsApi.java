package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.biz.account.model.support.CodeReq;

/**
 * 短信出站端口 (outbound port)。
 *
 * <p>迁移: 原直连静态工具 {@code com.zkl.scm.domain.sms.SmsMethod#sendCode};
 * 中台化后短信为独立能力, 经端口调用, 由入口 starter 侧真实实现覆盖默认实现。</p>
 *
 * @author KC
 */
public interface SmsApi {

    /**
     * 发送短信验证码/通知。
     *
     * @param codeReq 短信入参
     */
    void sendCode(CodeReq codeReq);
}
