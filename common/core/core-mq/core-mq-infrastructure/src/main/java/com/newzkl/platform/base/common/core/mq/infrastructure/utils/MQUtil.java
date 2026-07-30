package com.newzkl.platform.base.common.core.mq.infrastructure.utils;

import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.mq.domain.LocalMessageRepository;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.mq.infrastructure.producer.AbstractMQProducer;
import com.newzkl.platform.base.common.core.mq.infrastructure.producer.AbstractMQTransactionProducer;
import com.newzkl.platform.base.common.core.mq.infrastructure.config.MQProperties;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MQ 消息发送工具
 *
 * <p>迁移说明: 原 {@code send(LocalMessageVO)} 重载依赖本地消息业务模型，已移除；
 * 消息ID key 引用 {@link MQEnum#LOCAL_MESSAGE_ID_KEY}。</p>
 */
@Slf4j
@Component
public class MQUtil {

    private static AbstractMQProducer producer;
    private static AbstractMQTransactionProducer transactionProducer;
    private static LocalMessageRepository repository;

    @Autowired
    private void setProducer(AbstractMQProducer producer) {
        MQUtil.producer = producer;
    }

    @Autowired(required = false)
    private void setTransactionProducer(AbstractMQTransactionProducer transactionProducer) {
        MQUtil.transactionProducer = transactionProducer;
    }
    @Autowired
    public void setRepository(LocalMessageRepository repository) {
        MQUtil.repository = repository;
    }

    public static String getDefaultTopic() {
        return MQProperties.topic;
    }

    public static Message buildMessage(String topic, String tag, Long localMessageId, Object messageContent, String messageClass) {
        Object messageEvent = messageContent;
        if (messageContent instanceof String && JSONUtil.isTypeJSON(messageContent.toString())) {
            try {
                messageEvent = JSONUtil.toBean(messageContent.toString(), Class.forName(messageClass));
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException.", e);
                return null;
            }
        }
        Message message = MessageBuilder.of(messageEvent).topic(Opt.ofNullable(topic).orElse(getDefaultTopic())).tag(tag).build();
        message.putUserProperty(MQEnum.LOCAL_MESSAGE_ID_KEY, localMessageId.toString());
        return message;
    }

    public static SendResult send(String tag, Object messageContent) {
        return send(null, tag, SnowflakeIdAble.getSnowflakeId(), messageContent);
    }

    public static SendResult send(String topic, String tag, Long localMessageId, Object messageContent) {
        return send(topic, tag, localMessageId, messageContent, messageContent.getClass().getCanonicalName());
    }

    public static SendResult send(String tag, Long localMessageId, Object messageContent, String messageClass) {
        return send(null, tag, localMessageId, messageContent, messageClass);
    }

    public static SendResult send(String topic, String tag, Long localMessageId, Object messageContent, String messageClass) {
        Message message = buildMessage(topic, tag, localMessageId, messageContent, messageClass);
        SendResult sendResult;
        try {
            sendResult = producer.syncSend(message);
        } catch (Exception e) {
            log.error("消息发送失败.", e);
            return null;
        }
        return sendResult;
    }

    public static TransactionSendResult sendTransactional(String topic, String tag, Long localMessageId, Object messageContent, String messageClass, AbstractMQTransactionProducer transactionProducer, Object args) {
        Message message = buildMessage(topic, tag, localMessageId, messageContent, messageClass);
        TransactionSendResult sendResult;
        try {
            sendResult = transactionProducer.sendMessageInTransaction(message, args);
        } catch (Exception e) {
            log.error("消息发送失败.", e);
            return null;
        }
        return sendResult;
    }

    public static void sendAsync(String topic, String tag, Long localMessageId, Object messageContent, String messageClass, String hashKey) {
        Message message = buildMessage(topic, tag, localMessageId, messageContent, messageClass);
        producer.asyncSend(message, hashKey);
    }

    public static SendResult sendAsync(String topic, String tag, Long localMessageId, Object messageContent, String messageClass, SendCallback callback) {
        Message message = buildMessage(topic, tag, localMessageId, messageContent, messageClass);
        producer.asyncSend(message, callback);
        return null;
    }

    public void setMqProducer(AbstractMQProducer producer) {
        MQUtil.producer = producer;
    }

    /**
     * 发送延迟消息（支持延迟级别 1-18）
     *
     * @param topic          消息主题
     * @param tag            消息标签
     * @param localMessageId 本地消息ID
     * @param messageContent 消息内容对象
     * @param delayTimeLevel 延迟级别（1-18）
     * @return 发送结果
     */
    public static SendResult sendDelayed(String topic, String tag, Long localMessageId, Object messageContent, int delayTimeLevel) {
        if (messageContent == null) {
            log.error("messageContent cannot be null for delayed message");
            return null;
        }

        Message message = buildMessage(topic, tag, localMessageId, messageContent, messageContent.getClass().getCanonicalName());
        message.setDelayTimeLevel(delayTimeLevel);
        try {
            return producer.syncSend(message);
        } catch (Exception e) {
            log.error("延迟消息发送失败. topic={}, tag={}, delayLevel={}", topic, tag, delayTimeLevel, e);
            return null;
        }
    }

}
