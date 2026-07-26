package com.newzkl.platform.base.common.core.mq.consumer;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.newzkl.platform.base.common.core.mq.LocalMessageDomain;
import com.newzkl.platform.base.common.core.mq.enums.MQEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 幂等本地消息消费者基类。
 *
 * <p>移植自 adopt-chicken {@code AbstractMessageMQPushConsumer}; 基于 Base core-rocketmq
 * {@link AbstractMQPushConsumer} 泛型自动反序列化, 消费前查 local_message 做 CAS 幂等,
 * 再委派给 {@link AbstractMessageMQPushConsumerHandler}。去 TraceIdHolder / dev fixId 逻辑。</p>
 *
 * @param <T> 消息类型
 * @author fang
 */
@Slf4j
public abstract class AbstractMessageMQPushConsumer<T> extends AbstractMQPushConsumer<T> {

    /**
     * 懒加载获取本地幂等 Handler。
     *
     * @return Handler 实例
     */
    private AbstractMessageMQPushConsumerHandler getConsumerHandler() {
        return SpringUtil.getBean(AbstractMessageMQPushConsumerHandler.LOCAL_HANDLER_BEAN_NAME,
                AbstractMessageMQPushConsumerHandler.class);
    }

    @Override
    public boolean process(T message, Map<String, Object> extMap) {
        // 获取本地消息 id
        String localMessageId = MapUtil.getStr(extMap, MQEnum.LOCAL_MESSAGE_ID_KEY);
        Long messageId = null;
        if (StrUtil.isNotBlank(localMessageId) && NumberUtil.isNumber(localMessageId)) {
            messageId = Long.parseLong(localMessageId);
        }
        try {
            return getConsumerHandler().handle(message, messageId, extMap, this);
        } catch (Exception e) {
            log.error("消费异常", e);
            if (messageId != null) {
                SpringUtil.getBean(LocalMessageDomain.class)
                        .consumeFail(messageId, ExceptionUtil.stacktraceToString(e));
            }
            throw e;
        }
    }

    /**
     * 远程消费 — 子类实现业务处理。
     *
     * @param message 消息体
     * @param extMap  消息附加属性
     */
    public abstract void remoteProcess(T message, Map<String, Object> extMap);
}
