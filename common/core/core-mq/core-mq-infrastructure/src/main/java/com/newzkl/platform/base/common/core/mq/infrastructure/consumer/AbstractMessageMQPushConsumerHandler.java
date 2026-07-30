package com.newzkl.platform.base.common.core.mq.infrastructure.consumer;

import com.newzkl.platform.base.common.core.mq.domain.LocalMessageDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 本地幂等消费处理器
 *
 * <p>移植自模板 {@code AbstractMessageMQPushConsumerHandler}; 去 MQConsumedEvent 事件发布。</p>
 *
 * @author fang
 */
@Slf4j
@Component(AbstractMessageMQPushConsumerHandler.LOCAL_HANDLER_BEAN_NAME)
@RequiredArgsConstructor
public class AbstractMessageMQPushConsumerHandler {

    /**
     * 本地 Handler Bean 名称
     */
    public static final String LOCAL_HANDLER_BEAN_NAME = "messageMQPushConsumerHandler";

    private final LocalMessageDomain localMessageDomain;

    /**
     * 本地消息处理
     *
     * @param message        消息体
     * @param localMessageId 本地消息 id
     * @param extMap         消息附加属性
     * @param consumer       调用方消费者
     * @param <T>            消息类型
     * @return true 处理成功 / false 需重新消费
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> boolean handle(T message, Long localMessageId, Map<String, Object> extMap,
                              AbstractMessageMQPushConsumer<T> consumer) {
        return doHandle(message, localMessageId, extMap, consumer);
    }

    /**
     * 幂等消费主流程
     *
     * @param message        消息体
     * @param localMessageId 本地消息 id
     * @param extMap         消息附加属性
     * @param consumer       调用方消费者
     * @param <T>            消息类型
     * @return true 处理成功 / false 需重新消费
     */
    protected <T> boolean doHandle(T message, Long localMessageId, Map<String, Object> extMap,
                                   AbstractMessageMQPushConsumer<T> consumer) {
        // 无本地消息 id (非本地消息通道) → 直接远程消费, 不做幂等
        if (localMessageId == null) {
            consumer.remoteProcess(message, extMap);
            return true;
        }
        if (!localMessageDomain.exists(localMessageId)) {
            return false;
        }
        // 本地消费抢占 (CAS)
        boolean consumeState = localMessageDomain.consume(localMessageId);
        if (!consumeState) {
            log.warn("MQ异常: 重复的消息: localMessageId : {}", localMessageId);
            return true;
        }
        // 远程消费
        consumer.remoteProcess(message, extMap);
        return true;
    }
}
