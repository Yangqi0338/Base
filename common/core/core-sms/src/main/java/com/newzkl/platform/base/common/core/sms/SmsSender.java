package com.newzkl.platform.base.common.core.sms;

import com.newzkl.platform.base.common.core.sms.enums.SmsEnum;

import java.util.List;

/**
 * 短信发送端口
 *
 * <p>core-sms 对外唯一发送入口: 任何 biz 只依赖本接口即可发短信,
 * 三方通道 (连连等) 与商户凭证由本模块内部持有, biz 侧不再配置 properties。</p>
 *
 * @author KC
 */
public interface SmsSender {

    /**
     * 按显式模板号发送
     *
     * @param req 发送入参 (需已填好 {@code templateId})
     * @return 三方受理成功返回 {@code true}
     */
    boolean send(SmsSendReq req);

    /**
     * 按短信类型发送 (模板号由类型自动取)
     *
     * @param type   短信类型
     * @param phone  接收手机号
     * @param params 模板变量, 可为 {@code null}
     * @return 三方受理成功返回 {@code true}
     */
    default boolean send(SmsEnum.Type type, String phone, List<String> params) {
        return send(SmsSendReq.of(phone, type.getTemplateId(), params));
    }
}
