package com.newzkl.platform.base.common.core.rocketmq.base.consumer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.rocketmq.trace.common.OnsMessageTraceBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RocketMQ的消费者(Push模式)处理消息的接口
 */
@Slf4j
public abstract class AbstractMQPushConsumer<T> {

    @Getter
    @Setter
    private DefaultMQPushConsumer consumer;


    /**
     * 继承这个方法处理消息
     *
     * @param message 消息范型
     * @param extMap  存放消息附加属性的map, map中的key存放在 @link MessageExtConst 中
     * @return 处理结果
     */
    public abstract boolean process(T message, Map<String, Object> extMap);

    /**
     * 原生dealMessage方法，可以重写此方法自定义序列化和返回消费成功的相关逻辑
     *
     * @param list                       消息列表
     * @param consumeConcurrentlyContext 上下文
     * @return 消费状态
     */
    public ConsumeConcurrentlyStatus dealMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt messageExt : list) {
            OnsMessageTraceBean.consumerTrace(messageExt);
            log.debug("receive msgId: {}, tags : {}", messageExt.getMsgId(), messageExt.getTags());
            // parse message body
            T t = parseMessage(messageExt);
            // parse ext properties
            Map<String, Object> ext = parseExtParam(messageExt);
            if (null != t && !process(t, ext)) {
                log.warn("consume fail , ask for re-consume , msgId: {}", messageExt.getMsgId());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    /**
     * 原生dealMessage方法，可以重写此方法自定义序列化和返回消费成功的相关逻辑
     *
     * @param list                  消息列表
     * @param consumeOrderlyContext 上下文
     * @return 处理结果
     */
    public ConsumeOrderlyStatus dealMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        for (MessageExt messageExt : list) {
            OnsMessageTraceBean.consumerTrace(messageExt);
            log.info("receive msgId: {}, tags : {}", messageExt.getMsgId(), messageExt.getTags());
            T t = parseMessage(messageExt);
            Map<String, Object> ext = parseExtParam(messageExt);
            if (null != t && !process(t, ext)) {
                log.warn("consume fail , ask for re-consume , msgId: {}", messageExt.getMsgId());
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

    /**
     * 反序列化解析消息
     *
     * @param message 消息体
     * @return 序列化结果
     */
    protected T parseMessage(MessageExt message) {
        if (message == null || message.getBody() == null) {
            return null;
        }
        final Class<?> clazz = ClassUtil.getTypeArgument(this.getClass());
        if (clazz != null) {
            try {
                return JSONUtil.toBean(new String(message.getBody()), clazz, false);
            } catch (ConvertException e) {
                log.error("parse message json fail : {}", e.getMessage());
            }
        } else {
            log.warn("Parse msg error. {}", message);
        }
        return null;
    }

    protected Map<String, Object> parseExtParam(MessageExt message) {
        Map<String, Object> extMap = new HashMap<>();
        // parse message property
        extMap.putAll(BeanUtil.beanToMap(message));
        extMap.putAll(message.getProperties());
        return extMap;
    }
}
