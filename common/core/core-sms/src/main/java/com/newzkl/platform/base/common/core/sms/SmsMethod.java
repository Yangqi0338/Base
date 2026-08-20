package com.newzkl.platform.base.common.core.sms;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONUtil;


import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;


/**
 * 短信发送工具类，仅负责发送消息，无持久化存储
 */
@Slf4j
@Component
public class SmsMethod {

    /**
     * 联麓短信API客户端
     */
    private static SmsApi api;

    /**
     * 发送短信
     * @ext 联麓平台
     * @param codeReq 短信发送请求
     * @return 发送成功返回 true，否则返回 false
     */
    public static boolean sendCode(CodeReq codeReq) {
        SmsEnum.Type smsType = codeReq.getType();
        try {
            codeReq.setTemplateId(smsType.getTemplateId());
            return sendCodeLianLu(codeReq);
        } catch (Exception e) {
            throw new PlatformException(BaseErrorCode.CUSTOM, e.getMessage());
        }
    }

    /**
     * 通过联麓平台发送短信验证码或通知
     *
     * @param codeReq 短信发送请求
     * @return 发送成功返回 true，否则返回 false
     */
    private static boolean sendCodeLianLu(CodeReq codeReq) {
        SmsReq.LianLuSendMsgReq sendMsgReq = new SmsReq.LianLuSendMsgReq();
        sendMsgReq.setTemplateId(codeReq.getTemplateId());
        sendMsgReq.setPhoneNumberSet(Collections.singletonList(codeReq.getPhone()));
        sendMsgReq.setType(SmsConfig.LianLuSmsConfig.Type);
        if (CollUtil.isNotEmpty(codeReq.getParams())) {
            sendMsgReq.setTemplateParamSet(codeReq.getParams());
        }
        SmsRes.LianLuSendMsgRes res;
        try {
            res = api.lianLuSendMsg(sendMsgReq);
        } catch (Exception e) {
            log.error("短信发送失败，异常信息为：{}, {}",
                    e.getMessage(),
                    JSONUtil.toJsonStr(sendMsgReq));
            return false;
        }
        if (res == null || !res.isSuccess()) {
            log.error("短信发送失败，异常信息为：{}, {}",
                    Opt.ofNullable(res).map(SmsRes.LianLuSendMsgRes::getMessage).orElse(""),
                    JSONUtil.toJsonStr(sendMsgReq));
            return false;
        }
        return true;
    }

    /**
     * 注入联麓短信API客户端
     *
     * @param smsApi 短信API客户端
     */
    @Autowired(required = false)
    public void setSmsApi(SmsApi smsApi) {
        SmsMethod.api = smsApi;
    }
}
