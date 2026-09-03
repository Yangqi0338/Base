package com.newzkl.platform.base.biz.order.action.mq;

import com.newzkl.platform.base.biz.order.domain.service.OrderDomain;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateRecordRPC;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateRecordEntity;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 订单状态记录消费者
 *
 * <p>消费订单状态变更事件, 将 RPC 传输模型转换为领域实体后落库</p>
 *
 * @author KC
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.ORDER_STATE_RECORD_MESSAGE, tag = MQ.Tag.ORDER_STATE_RECORD_EVENT)
public class OrderStateRecordConsumer extends AbstractMessageMQPushConsumer<OrderStateRecordRPC> {

    @Autowired
    private OrderDomain orderDomain;

    /**
     * 处理订单状态记录消息
     *
     * @param message 订单状态记录 RPC 传输模型
     * @param extMap  消息扩展参数
     */
    @Override
    public void remoteProcess(OrderStateRecordRPC message, Map<String, Object> extMap) {
        log.info("开始消费订单状态记录消息, 交易单号: {}, 变更后状态: {}", message.getOrderNo(), message.getAfterOrderState());
        OrderStateRecordEntity entity = new OrderStateRecordEntity();
        BeanUtils.copyProperties(message, entity);
        orderDomain.createStateRecord(entity);
        log.info("订单状态记录消息消费成功, 交易单号: {}", message.getOrderNo());
    }
}
