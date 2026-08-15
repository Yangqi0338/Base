package com.newzkl.platform.base.common.core.sms;


import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 短信静态门面
 *
 * <p>参 adopt-chicken {@code SmsMethod} 范式: 给拿不到容器注入的调用点 (静态工具、枚举、
 * 非 Bean 上下文) 一条发送通路。能注入的地方优先直接注 {@code SmsSender}。</p>
 *
 * <p>只负责发送, 不做持久化, 不做限频 —— 限频属业务策略, 由调用方 (如验证码控制器) 决定。</p>
 *
 * @author KC
 */
@Component
public class SmsMethod {

    /**
     * 发送端口, 由容器回填
     */
    private static SmsSender sender;

    /**
     * 按显式模板号发送
     *
     * @param req 发送入参 (需已填好 {@code templateId})
     * @return 三方受理成功返回 {@code true}
     */
    public static boolean send(SmsSendReq req) {
        return sender.send(req);
    }

    /**
     * 按短信类型发送 (模板号由类型自动取)
     *
     * @param type   短信类型
     * @param phone  接收手机号
     * @param params 模板变量, 可为 {@code null}
     * @return 三方受理成功返回 {@code true}
     */
    public static boolean send(SmsEnum.Type type, String phone, List<String> params) {
        return sender.send(type, phone, params);
    }

    /**
     * 回填发送端口
     *
     * @param smsSender 短信发送端口
     */
    @Autowired(required = false)
    public void setSender(SmsSender smsSender) {
        SmsMethod.sender = smsSender;
    }
}
