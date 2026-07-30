package com.newzkl.platform.base.biz.sys.application.service.impl;

import com.newzkl.platform.base.biz.sys.application.service.CodeService;
import com.newzkl.platform.base.biz.sys.model.code.req.CodeReq;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.sms.SmsSendReq;
import com.newzkl.platform.base.common.core.sms.SmsSender;
import com.newzkl.platform.base.common.core.sms.enums.SmsEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 短信验证码编排实现
 *
 * <p>迁移自 new-scm {@code message.application.service.impl.CodeServiceImpl},
 * 三方通道细节下沉到 {@code core-sms} 的 {@code SmsSender}, 本层不持有连连凭证。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {

    private final SmsSender smsSender;

    @Override
    public boolean sendCodeLianLu(CodeReq codeReq) {
        return smsSender.send(SmsSendReq.of(
                codeReq.getPhone(), codeReq.getTemplateId(), codeReq.getParams()));
    }

    @Override
    public void sendNotifyCode(CodeReq codeReq) {
        SmsEnum.Type smsType = SmsEnum.Type.getByCode(codeReq.getType());
        try {
            codeReq.setTemplateId(smsType.getTemplateId());
            sendCodeLianLu(codeReq);
        } catch (Exception e) {
            ThrowsException.exception(BaseErrorCode.CUSTOM, e.getMessage());
        }
    }
}
