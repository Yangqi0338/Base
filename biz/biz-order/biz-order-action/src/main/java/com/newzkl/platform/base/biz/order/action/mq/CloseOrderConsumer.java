package com.newzkl.platform.base.biz.order.action.mq;

import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;

import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 超时关闭订单消息消费者
 * @author sijiwang
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.TIME_OUT_CLOSE_ORDER_MESSAGE, tag = MQ.Tag.TIME_OUT_CLOSE_ORDER_EVENT)
public class CloseOrderConsumer extends AbstractMessageMQPushConsumer<String> {
    @Autowired
    private OrderService orderService;

    @Override
    public void remoteProcess(String orderNo, Map<String, Object> extMap) {
        if (orderNo != null){
            orderService.closeOrder(orderNo);
        }
    }
}
