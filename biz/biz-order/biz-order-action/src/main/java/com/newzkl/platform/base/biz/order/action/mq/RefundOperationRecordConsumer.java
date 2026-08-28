package com.newzkl.platform.base.biz.order.action.mq;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundOperationRecordRepository;
import com.newzkl.platform.base.biz.order.facade.model.order.RefundOperationRecordRPC;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordDTO;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;


import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.dubbo.config.annotation.DubboReference;

import java.io.Serializable;
import java.util.Map;

/**
 * 售后单操作记录消费者
 * @author sijiwang
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.REFUND_OPERATION_RECORD_MESSAGE, tag = MQ.Tag.REFUND_OPERATION_RECORD_EVENT)
public class RefundOperationRecordConsumer extends AbstractMessageMQPushConsumer<RefundOperationRecordRPC> {
    @DubboReference
    private RefundOperationRecordRepository refundOperationRecordRepository;
    @Override
    public void remoteProcess(RefundOperationRecordRPC message, Map<String, Object> extMap) {
        log.info("售后单操作记录消费者:{}", message);
        RefundOperationRecordDTO dto = TransferUtils.transfer(message, RefundOperationRecordDTO.class);
        refundOperationRecordRepository.save(dto);
    }
}
