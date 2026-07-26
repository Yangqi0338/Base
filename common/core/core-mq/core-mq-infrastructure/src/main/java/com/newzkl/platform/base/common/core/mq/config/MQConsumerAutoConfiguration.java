package com.newzkl.platform.base.common.core.mq.config;

import com.newzkl.platform.base.common.core.mq.enums.MQEnum;
import com.newzkl.platform.base.common.core.mq.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.consumer.AbstractMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.constant.OnsTraceConstants;
import com.newzkl.platform.base.common.core.mq.trace.dispatch.impl.AsyncTraceAppender;
import com.newzkl.platform.base.common.core.mq.trace.dispatch.impl.AsyncTraceDispatcher;
import com.newzkl.platform.base.common.core.mq.trace.tracehook.OnsConsumeMessageHookImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * 自动装配消息消费者。
 */
@Slf4j
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQConsumerAutoConfiguration extends MQBaseAutoConfiguration implements InitializingBean {

    private AsyncTraceDispatcher asyncTraceDispatcher;
    /**
     * 维护一份 map 用于检测是否用同样的 consumerGroup 订阅了不同的 topic+tag。
     */
    private Map<String, String> validConsumerMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQConsumer.class);
        if (!CollectionUtils.isEmpty(beans) && mqProperties.getTraceEnabled()) {
            initAsyncAppender();
        }
        validConsumerMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            publishConsumer(entry.getKey(), entry.getValue());
        }
        validConsumerMap = null;
    }

    private AsyncTraceDispatcher initAsyncAppender() {
        if (asyncTraceDispatcher != null) {
            return asyncTraceDispatcher;
        }
        try {
            Properties tempProperties = new Properties();
            tempProperties.put(OnsTraceConstants.MaxMsgSize, "128000");
            tempProperties.put(OnsTraceConstants.AsyncBufferSize, "2048");
            tempProperties.put(OnsTraceConstants.MaxBatchNum, "1");
            tempProperties.put(OnsTraceConstants.WakeUpNum, "1");
            tempProperties.put(OnsTraceConstants.NAMESRV_ADDR, mqProperties.getNameServerAddress());
            tempProperties.put(OnsTraceConstants.InstanceName, UUID.randomUUID().toString());
            AsyncTraceAppender asyncAppender = new AsyncTraceAppender(tempProperties);
            asyncTraceDispatcher = new AsyncTraceDispatcher(tempProperties);
            asyncTraceDispatcher.start(asyncAppender, "DEFAULT_WORKER_NAME");
        } catch (MQClientException e) {
            log.error("init async trace appender failed.", e);
        }
        return asyncTraceDispatcher;
    }

    private void publishConsumer(String beanName, Object bean) throws Exception {
        MQConsumer mqConsumer = applicationContext.findAnnotationOnBean(beanName, MQConsumer.class);
        if (StringUtils.isEmpty(mqProperties.getNameServerAddress())) {
            throw new RuntimeException("name server address must be defined");
        }
        Assert.notNull(mqConsumer.consumerGroup(), "consumer's consumerGroup must be defined");
        Assert.notNull(mqConsumer.topic(), "consumer's topic must be defined");
        if (!AbstractMQPushConsumer.class.isAssignableFrom(bean.getClass())) {
            throw new RuntimeException(bean.getClass().getName() + " - consumer未实现Consumer抽象类");
        }
        Environment environment = applicationContext.getEnvironment();

        String consumerGroup = environment.resolvePlaceholders(mqConsumer.consumerGroup());
        String topic = environment.resolvePlaceholders(mqConsumer.topic());
        String tags = "*";
        if (mqConsumer.tag().length == 1) {
            tags = environment.resolvePlaceholders(mqConsumer.tag()[0]);
        } else if (mqConsumer.tag().length > 1) {
            tags = StringUtils.join(mqConsumer.tag(), "||");
        }

        if (ArrayUtils.contains(environment.getActiveProfiles(), "local")) {
            consumerGroup += "-local";
            tags += "_local";
        }

        if (!StringUtils.isEmpty(validConsumerMap.get(consumerGroup))) {
            String exist = validConsumerMap.get(consumerGroup);
            throw new RuntimeException("消费组重复订阅，请新增消费组用于新的topic和tag组合: " + consumerGroup + "已经订阅了" + exist);
        } else {
            validConsumerMap.put(consumerGroup, topic + "-" + tags);
        }

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(mqProperties.getNameServerAddress());
        consumer.setMessageModel(MessageModel.valueOf(mqConsumer.messageMode().getMode()));
        consumer.subscribe(topic, tags);
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.setVipChannelEnabled(mqProperties.getVipChannelEnabled());
        AbstractMQPushConsumer abstractMQPushConsumer = (AbstractMQPushConsumer) bean;
        if (MQEnum.ConsumeMode.CONCURRENTLY == mqConsumer.consumeMode()) {
            consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) ->
                    abstractMQPushConsumer.dealMessage(list, consumeConcurrentlyContext));
        } else if (MQEnum.ConsumeMode.ORDERLY == mqConsumer.consumeMode()) {
            consumer.registerMessageListener((List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) ->
                    abstractMQPushConsumer.dealMessage(list, consumeOrderlyContext));
        } else {
            throw new RuntimeException("unknown consume mode ! only support CONCURRENTLY and ORDERLY");
        }
        abstractMQPushConsumer.setConsumer(consumer);

        if (mqProperties.getTraceEnabled()) {
            try {
                consumer.getDefaultMQPushConsumerImpl().registerConsumeMessageHook(
                        new OnsConsumeMessageHookImpl(asyncTraceDispatcher));
            } catch (Throwable e) {
                log.error("system mqtrace hook init failed ,maybe can't send msg trace data");
            }
        }

        consumer.start();

        log.info(String.format("%s is ready to subscribe message", bean.getClass().getName()));
    }

}
