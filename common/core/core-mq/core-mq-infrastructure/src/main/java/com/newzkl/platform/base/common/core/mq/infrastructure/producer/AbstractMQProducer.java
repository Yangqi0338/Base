package com.newzkl.platform.base.common.core.mq.infrastructure.producer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.mq.domain.LocalMessageDomain;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQProducer;
import com.newzkl.platform.base.common.core.mq.infrastructure.config.MQProperties;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MessageBuilder;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.mq.model.exception.MQException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by yipin on 2017/6/27
 * RocketMQ的生产者的抽象基类
 *
 * <p>本地消息落库编排: 入参统一为 {@link LocalMessageDTO}。发送前先经
 * {@link LocalMessageDomain} 落库创建本地消息(无 id 则新建, 失败则取消发送),
 * 落库成功后直接投递 MQ(先落库再发送, 无需额外延时); 发送结果回写发送状态
 * (成功 SUCCESS / 失败 FAIL)。</p>
 */
@Slf4j
@Component
@MQProducer
public class AbstractMQProducer {

    private static final MessageQueueSelector messageQueueSelector = new SelectMessageQueueByHash();

    @Getter
    private DefaultMQProducer producer;

    @Autowired
    private LocalMessageDomain localMessageDomain;

    @Autowired
    public void setProducer(@Qualifier("exposeProducer") DefaultMQProducer producer) {
        this.producer = producer;
    }

    /**
     * 同步发送消息(先落库创建本地消息, 再投递 MQ)
     *
     * @param dto 本地消息 DTO(含 topic/tag/messageContent/messageClass; id 为空则由生产者落库创建)
     * @return 发送结果
     * @throws MQException 消息异常
     */
    public SendResult syncSend(LocalMessageDTO dto) throws MQException {
        Long messageId = dto.getId();
        if (messageId == null) {
            messageId = localMessageDomain.preSave(dto);
            if (messageId == null) {
                log.error("本地消息落库失败，取消发送，topic : {}, tag : {}", dto.getTopic(), dto.getTag());
                throw new MQException("本地消息落库失败，取消发送，topic:" + dto.getTopic());
            }
        }
        Message message = buildMessage(dto, messageId);
        SendResult sendResult = new SendResult();
        try {
            producer.sendOneway(message);
            this.doAfterSyncSend(message, sendResult);
            localMessageDomain.messageSendUpdate(messageId, MQEnum.SendState.SUCCESS);
        } catch (Exception e) {
            log.error("消息发送失败，topic : {}, tag : {}, msgId : {}", dto.getTopic(), dto.getTag(), messageId, e);
            // 失败也落库回写
            localMessageDomain.messageSendUpdate(messageId, MQEnum.SendState.FAIL);
            throw new MQException("消息发送失败，topic :" + dto.getTopic() + ",e:" + e.getMessage());
        }
        return sendResult;
    }

    /**
     * 顺序同步发送消息, hashKey 为空时回落普通同步发送
     *
     * @param dto     本地消息 DTO
     * @param hashKey 用于 hash 后选择 queue 的 key
     * @return 发送结果
     * @throws MQException 消息异常
     */
    public SendResult syncSendOrderly(LocalMessageDTO dto, String hashKey) throws MQException {
        if (StrUtil.isEmpty(hashKey)) {
            return syncSend(dto);
        }
        Long messageId = localMessageDomain.preSave(dto);
        if (messageId == null) {
            log.error("本地消息落库失败，取消发送，topic : {}, tag : {}", dto.getTopic(), dto.getTag());
            throw new MQException("本地消息落库失败，取消发送，topic:" + dto.getTopic());
        }
        Message message = buildMessage(dto, messageId);
        SendResult sendResult;
        try {
            sendResult = producer.send(message, messageQueueSelector, hashKey);
            log.debug("send rocketmq message orderly ,messageId : {}", sendResult.getMsgId());
            this.doAfterSyncSend(message, sendResult);
            localMessageDomain.messageSendUpdate(messageId, MQEnum.SendState.SUCCESS);
        } catch (Exception e) {
            log.error("顺序消息发送失败，topic : {}, tag : {}, msgId : {}", dto.getTopic(), dto.getTag(), messageId, e);
            localMessageDomain.messageSendUpdate(messageId, MQEnum.SendState.FAIL);
            throw new MQException("顺序消息发送失败，topic :" + dto.getTopic() + ",e:" + e.getMessage());
        }
        return sendResult;
    }

    /**
     * 重写此方法处理发送后的逻辑
     *
     * @param message    发送消息体
     * @param sendResult 发送结果
     */
    public void doAfterSyncSend(Message message, SendResult sendResult) {
    }

    /**
     * 异步发送消息
     *
     * @param dto          本地消息 DTO
     * @param sendCallback 回调
     * @throws MQException 消息异常
     */
    public void asyncSend(LocalMessageDTO dto, SendCallback sendCallback) throws MQException {
        Long messageId = localMessageDomain.preSave(dto);
        if (messageId == null) {
            log.error("本地消息落库失败，取消发送，topic : {}, tag : {}", dto.getTopic(), dto.getTag());
            throw new MQException("本地消息落库失败，取消发送，topic:" + dto.getTopic());
        }
        Message message = buildMessage(dto, messageId);
        try {
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    sendCallback.onSuccess(sendResult);
                    doAfterSyncSend(message, sendResult);
                    localMessageDomain.messageSendUpdate(messageId, MQEnum.SendState.SUCCESS);
                }

                @Override
                public void onException(Throwable throwable) {
                    localMessageDomain.messageSendUpdate(messageId, MQEnum.SendState.FAIL);
                    sendCallback.onException(throwable);
                }
            });
            log.debug("send rocketmq message async");
        } catch (Exception e) {
            log.error("消息发送失败，topic : {}, tag : {}, msgId : {}", dto.getTopic(), dto.getTag(), messageId, e);
            localMessageDomain.messageSendUpdate(messageId, MQEnum.SendState.FAIL);
            throw new MQException("消息发送失败，topic :" + dto.getTopic() + ",e:" + e.getMessage());
        }
    }

    /**
     * 仅落库创建本地消息, 不投递 MQ。
     *
     * <p>供唤醒/延迟消息使用: 先落一条 sendState=WAIT 且 canConsume=NO 的待发送记录,
     * 待业务侧通过 outKey 唤醒后由 {@code LocalMessageJob} 扫描发送。</p>
     *
     * @param dto 本地消息 DTO
     * @return 本地消息 id, 落库失败返回 null
     */
    public Long initMessage(LocalMessageDTO dto) {
        return localMessageDomain.preSave(dto);
    }

    /**
     * 将本地消息 DTO 转换为 RocketMQ Message, 并把本地消息 id 挂到消息附加属性上
     * (供消费端按 LOCAL_MESSAGE_ID_KEY 做幂等)。
     *
     * @param dto       本地消息 DTO
     * @param messageId 本地消息 id
     * @return RocketMQ Message
     */
    public Message buildMessage(LocalMessageDTO dto, Long messageId) {
        Object messageEvent = dto.getMessageContent();
        if (JSONUtil.isTypeJSON(dto.getMessageContent())) {
            try {
                messageEvent = JSONUtil.toBean(dto.getMessageContent(), Class.forName(dto.getMessageClass()));
            } catch (ClassNotFoundException e) {
                log.error("消息内容反序列化失败, messageClass : {}", dto.getMessageClass(), e);
                throw new MQException("消息内容反序列化失败, messageClass:" + dto.getMessageClass());
            }
        }
        Message message = MessageBuilder.of(messageEvent)
                .topic(dto.getTopic())
                .tag(dto.getTag())
                .build();
        if (dto.getDelayTimeLevel() != null) {
            message.setDelayTimeLevel(dto.getDelayTimeLevel());
        }
        message.putUserProperty(MQEnum.LOCAL_MESSAGE_ID_KEY, messageId.toString());
        return message;
    }

    /**
     * 按 outKey 唤醒本地消息: 可消费标识翻为 YES, 由 LocalMessageJob 扫描发送
     *
     * @param outKey 外部唯一键
     * @return 是否唤醒成功
     */
    public boolean wakeUp(String outKey) {
        return localMessageDomain.wakeUp(outKey);
    }
}