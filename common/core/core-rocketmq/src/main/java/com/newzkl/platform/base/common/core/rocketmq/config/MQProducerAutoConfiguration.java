package com.newzkl.platform.base.common.core.rocketmq.config;

import com.newzkl.platform.base.common.core.rocketmq.annotation.MQProducer;
import com.newzkl.platform.base.common.core.rocketmq.annotation.MQTransactionProducer;
import com.newzkl.platform.base.common.core.rocketmq.base.producer.AbstractMQTransactionProducer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自动装配消息生产者。
 */
@Slf4j
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQProducerAutoConfiguration extends MQBaseAutoConfiguration implements InitializingBean {

    @Setter
    private static DefaultMQProducer producer;

    /**
     * 暴露默认生产者。仅存在消费者的项目无需构建生产者。
     *
     * @return 默认生产者，无生产者 Bean 时返回 null
     * @throws Exception 生产者启动异常
     */
    @Bean
    public DefaultMQProducer exposeProducer() throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQProducer.class);
        if (CollectionUtils.isEmpty(beans)) {
            return null;
        }
        if (producer == null) {
            Assert.notNull(mqProperties.getProducerGroup(), "producer group must be defined");
            Assert.notNull(mqProperties.getNameServerAddress(), "name server address must be defined");
            producer = new DefaultMQProducer(mqProperties.getProducerGroup());
            producer.setNamesrvAddr(mqProperties.getNameServerAddress());
            producer.setSendMsgTimeout(mqProperties.getSendMsgTimeout());
            producer.setSendMessageWithVIPChannel(mqProperties.getVipChannelEnabled());
            producer.start();
        }
        return producer;
    }

    /**
     * 装配事务生产者。
     */
    @Override
    public void afterPropertiesSet() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQTransactionProducer.class);
        if (CollectionUtils.isEmpty(beans)) {
            return;
        }
        ExecutorService executorService = new ThreadPoolExecutor(beans.size(), beans.size() * 2, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000), r -> {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        });
        Environment environment = applicationContext.getEnvironment();
        beans.forEach((name, value) -> {
            try {
                AbstractMQTransactionProducer beanObj = AbstractMQTransactionProducer.class.cast(value);
                MQTransactionProducer anno = beanObj.getClass().getAnnotation(MQTransactionProducer.class);

                TransactionMQProducer transactionProducer = new TransactionMQProducer(environment.resolvePlaceholders(anno.producerGroup()));
                transactionProducer.setNamesrvAddr(mqProperties.getNameServerAddress());
                transactionProducer.setExecutorService(executorService);
                transactionProducer.setTransactionListener(beanObj);
                transactionProducer.start();
                beanObj.setProducer(transactionProducer);
            } catch (Exception e) {
                log.error("build transaction producer error.", e);
            }
        });
    }
}
