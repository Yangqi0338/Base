package com.newzkl.platform.base.biz.order.application.order.mq;

import com.newzkl.platform.base.common.core.rocketmq.annotation.MQConsumer;
import com.zkl.scm.infrastructure.mq.base.consumer.AbstractMessageMQPushConsumer;
import com.zkl.scm.model.constants.common.MQ;
import com.newzkl.platform.base.biz.order.domain.service.OrderStateRecordDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderStateRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 订单状态记录消费者 消费订单状态变更事件，保存订单状态记录
 *
 * @author sijiwang
 */
@Slf4j
@MQConsumer(topic = MQ.SCM_ORDER, consumerGroup = MQ.Tag.ORDER_STATE_RECORD_MESSAGE, tag = MQ.Tag.ORDER_STATE_RECORD_EVENT)
@RequiredArgsConstructor
public class OrderStateRecordConsumer extends AbstractMessageMQPushConsumer<OrderStateRecord> {

    /**
     * 引用订单状态记录RPC门面接口
     */
    private final OrderStateRecordDomain orderStateRecordDomainService;

    /**
     * 处理MQ消息核心逻辑
     *
     * @param message 订单状态记录RPC传输模型
     * @param extMap  消息扩展参数
     */
    @Override
    public void remoteProcess(OrderStateRecord message, Map<String, Object> extMap) {
        try {
            log.info("开始消费订单状态记录消息，订单ID：{}，变更后状态：{}，消息内容：{}", message.getOrderNo(), message.getAfterOrderState(), message);

            // 调用RPC门面保存订单状态记录
            orderStateRecordDomainService.create(message);

            log.info("订单状态记录消息消费成功，订单ID：{}，记录ID：{}", message.getOrderNo(), message.getId());
        } catch (Exception e) {
            log.error("消费订单状态记录消息失败，订单ID：{}，原因：{}", message.getOrderNo(), e.getMessage(), e);
            // 可根据业务需求添加重试/告警逻辑
            throw new RuntimeException("消费订单状态记录消息失败", e);
        }
    }
}