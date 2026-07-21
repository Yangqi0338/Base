package com.newzkl.platform.base.common.core.rocketmq.trace.common;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.MDC;

import java.util.UUID;

@Slf4j
public class OnsMessageTraceBean {

    public static final String TRACE_ID = "traceId";


    public static void producerTrace(Message message) {
        try {
            String traceId = MDC.get(TRACE_ID);
            if (StrUtil.isBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
            }
            message.putUserProperty(TRACE_ID, traceId);
        } catch (Exception e) {
            log.warn("发送方启动trace链路记录失败。", e);
        }
    }

    public static void consumerTrace(Message message) {
        try {
            String traceId = message.getUserProperty(TRACE_ID);
            if (StrUtil.isBlank(traceId)) {
                traceId = RandomStringUtils.random(16, true, true).toLowerCase();
            }
            MDC.put(TRACE_ID, traceId);
        } catch (Exception e) {
            log.warn("消息接收方收取trace链路记录失败。", e);
        }
    }
}
