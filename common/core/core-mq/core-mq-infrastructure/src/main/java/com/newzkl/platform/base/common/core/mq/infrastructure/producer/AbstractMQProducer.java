package com.newzkl.platform.base.common.core.mq.infrastructure.producer;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQProducer;
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

/**
 * Created by yipin on 2017/6/27
 * RocketMQ的生产者的抽象基类
 */
@Slf4j
@Component
@MQProducer
public class AbstractMQProducer {

    private static final MessageQueueSelector messageQueueSelector = new SelectMessageQueueByHash();

    @Getter
    private DefaultMQProducer producer;

    @Autowired
    public void setProducer(@Qualifier("exposeProducer") DefaultMQProducer producer) {
        this.producer = producer;
    }

    /**
     * 同步发送消息
     *
     * @param message 消息体
     * @throws MQException 消息异常
     */
    public SendResult syncSend(Message message) throws MQException {
        SendResult sendResult = new SendResult();
        try {
            producer.sendOneway(message);
            this.doAfterSyncSend(message, sendResult);
        } catch (Exception e) {
            log.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
            throw new MQException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
        }
        return sendResult;
    }


    /**
     * 异步发送消息
     *
     * @param message 消息体
     * @param hashKey 用于hash后选择queue的key
     * @throws MQException 消息异常
     */
    public void asyncSend(Message message, String hashKey) throws MQException {
        if (StrUtil.isEmpty(hashKey)) {
            syncSend(message);
        } else {
            try {
                SendResult sendResult = producer.send(message, messageQueueSelector, hashKey);
                log.debug("send rocketmq message orderly ,messageId : {}", sendResult.getMsgId());
                this.doAfterSyncSend(message, sendResult);
            } catch (Exception e) {
                log.error("顺序消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
                throw new MQException("顺序消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
            }
        }
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
     * @param message      msgObj
     * @param sendCallback 回调
     * @throws MQException 消息异常
     */
    public void asyncSend(Message message, SendCallback sendCallback) throws MQException {
        try {
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    sendCallback.onSuccess(sendResult);
                    doAfterSyncSend(message, sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    sendCallback.onException(throwable);
                }
            });
            log.debug("send rocketmq message async");
        } catch (Exception e) {
            log.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
            throw new MQException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
        }
    }
}
