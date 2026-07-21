package com.newzkl.platform.base.common.core.rocketmq.annotation;

import com.newzkl.platform.base.common.core.rocketmq.MQEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * RocketMQ 消费者自动装配注解。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MQConsumer {
    String consumerGroup();

    String topic() default "${spring.rocketmq.topic}";

    /**
     * 广播模式消费： BROADCASTING；集群模式消费： CLUSTERING。
     *
     * @return 消息模式
     */
    MQEnum.MessageMode messageMode() default MQEnum.MessageMode.CLUSTERING;

    /**
     * 使用线程池并发消费: CONCURRENTLY；单线程消费: ORDERLY。
     *
     * @return 消费模式
     */
    MQEnum.ConsumeMode consumeMode() default MQEnum.ConsumeMode.CONCURRENTLY;

    String[] tag();
}
