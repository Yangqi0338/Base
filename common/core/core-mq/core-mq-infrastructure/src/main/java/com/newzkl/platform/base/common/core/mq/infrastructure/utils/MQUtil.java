package com.newzkl.platform.base.common.core.mq.infrastructure.utils;

import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mq.infrastructure.producer.AbstractMQProducer;
import com.newzkl.platform.base.common.core.mq.infrastructure.producer.AbstractMQTransactionProducer;
import com.newzkl.platform.base.common.core.mq.infrastructure.config.MQProperties;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
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
 * <p>对外保持业务事件发送入口, 内部把消息组装成完整的 {@link LocalMessageDTO} 后回调
 * {@link AbstractMQProducer}, 由生产者统一完成本地消息落库 + 投递;
 * 本工具不再直接落库, 也不再把本地消息 id 手工塞进 Message。</p>
 */
@Slf4j
@Component
public class MQUtil {

    private static AbstractMQProducer producer;
    private static AbstractMQTransactionProducer transactionProducer;

    @Autowired
    private void setProducer(AbstractMQProducer producer) {
        MQUtil.producer = producer;
    }

    @Autowired(required = false)
    private void setTransactionProducer(AbstractMQTransactionProducer transactionProducer) {
        MQUtil.transactionProducer = transactionProducer;
    }

    public static String getDefaultTopic() {
        return MQProperties.topic;
    }

    public static SendResult send(String tag, Object messageContent) {
        return send(null, tag, null, messageContent);
    }

    public static SendResult send(String topic, String tag, Long localMessageId, Object messageContent) {
        LocalMessageDTO dto = buildDTO(topic, tag, localMessageId, messageContent);
        SendResult sendResult;
        try {
            sendResult = producer.syncSend(dto);
        } catch (Exception e) {
            log.error("消息发送失败.", e);
            return null;
        }
        return sendResult;
    }

    public static TransactionSendResult sendTransactional(String topic, String tag, Long localMessageId, Object messageContent, String messageClass, AbstractMQTransactionProducer transactionProducer, Object args) {
        LocalMessageDTO dto = buildDTO(topic, tag, localMessageId, messageContent, messageClass);
        Message message = producer.buildMessage(dto, localMessageId);
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
        LocalMessageDTO dto = buildDTO(topic, tag, localMessageId, messageContent, messageClass);
        producer.syncSendOrderly(dto, hashKey);
    }

    public static SendResult sendAsync(String topic, String tag, Long localMessageId, Object messageContent, String messageClass, SendCallback callback) {
        LocalMessageDTO dto = buildDTO(topic, tag, localMessageId, messageContent, messageClass);
        producer.asyncSend(dto, callback);
        return null;
    }

    public static SendResult sendDelayed(String tag, Object messageContent, int delayTimeLevel) {
        return sendDelayed(null, tag, null, messageContent, delayTimeLevel);
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
        LocalMessageDTO dto = buildDTO(topic, tag, localMessageId, messageContent);
        dto.setDelayTimeLevel(delayTimeLevel);
        try {
            return producer.syncSend(dto);
        } catch (Exception e) {
            log.error("延迟消息发送失败. topic={}, tag={}, delayLevel={}", topic, tag, delayTimeLevel, e);
            return null;
        }
    }

    /**
     * 组装本地消息 DTO: 消息内容对象序列化为 JSON 字符串, 供生产者统一落库 + 投递。
     *
     * @param topic          消息主题
     * @param tag            消息标签
     * @param localMessageId 本地消息ID(可为空, 由生产者落库创建)
     * @param messageContent 消息内容对象或 JSON 字符串
     * @return 本地消息 DTO
     */
    private static LocalMessageDTO buildDTO(String topic, String tag, Long localMessageId, Object messageContent) {
        return buildDTO(topic, tag, localMessageId, messageContent, null);
    }

    /**
     * 组装本地消息 DTO(指定 outKey)。
     *
     * @param topic          消息主题
     * @param tag            消息标签
     * @param localMessageId 本地消息ID(可为空, 由生产者落库创建)
     * @param messageContent 消息内容对象或 JSON 字符串
     * @param outKey         外部唯一键(唤醒场景使用, 空则自动生成)
     * @return 本地消息 DTO
     */
    private static LocalMessageDTO buildDTO(String topic, String tag, Long localMessageId, Object messageContent, String outKey) {
        String content = null;
        String contentClass = null;
        if (JSONUtil.isTypeJSON((String) messageContent)) {
            content = (String) messageContent;
        } else if (messageContent != null) {
            content = messageContent.toString();
            contentClass = messageContent.getClass().getCanonicalName();
        }
        LocalMessageDTO dto = new LocalMessageDTO();
        dto.setTopic(Opt.ofNullable(topic).orElse(getDefaultTopic()));
        dto.setTag(tag);
        dto.setId(localMessageId);
        dto.setMessageContent(content);
        dto.setMessageClass(contentClass);
        dto.setOutKey(outKey);
        return dto;
    }

    /**
     * 仅落库一条待发送的本地消息(不投递 MQ), 等待业务侧经 {@link #wakeUp(String)} 唤醒后
     * 由 LocalMessageJob 扫描发送。
     *
     * @param topic          消息主题
     * @param tag            消息标签
     * @param outKey         外部唯一键
     * @param messageContent 消息内容对象
     * @return 本地消息 id
     */
    public static Long prepareDelayMessage(String topic, String tag, String outKey, Object messageContent) {
        LocalMessageDTO dto = buildDTO(topic, tag, null, messageContent, outKey);
        return producer.initMessage(dto);
    }

    /**
     * 唤醒延迟消息: 按 outKey 将可消费标识翻为 YES, 由 LocalMessageJob 扫描发送
     *
     * @param outKey 外部唯一键
     * @return 是否唤醒成功
     */
    public static boolean wakeUp(String outKey) {
        return producer.wakeUp(outKey);
    }
}