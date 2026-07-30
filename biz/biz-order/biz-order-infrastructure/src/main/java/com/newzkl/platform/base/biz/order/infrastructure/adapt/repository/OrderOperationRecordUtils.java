package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MQUtil;
import com.newzkl.platform.base.biz.order.model.support.MQ;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderStateRecord;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sijiwang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOperationRecordUtils {
    public static final String MQ_TOPIC = MQ.SCM_ORDER;
    public static final String MQ_TAG = MQ.Tag.ORDER_STATE_RECORD_EVENT;


    public void sendOrderNewRecordEvent(List<SpuOrder> spuOrderList,Integer beforeOrderState,Integer afterOrderState, Long operatorId,RoleEnum.CompanyRole operatorRoleId) {

        try {
            spuOrderList.forEach(spuOrder -> {
                OrderStateRecord orderStateRecord = new OrderStateRecord();
                orderStateRecord.setOrderNo(spuOrder.getOrderNo());
                orderStateRecord.setSkuOrderNo(spuOrder.getSpuOrderNo());
                orderStateRecord.setBeforeOrderState(beforeOrderState);
                orderStateRecord.setBeforeStateDesc(OrderEnum.State.getByCode(beforeOrderState).getInfo());
                orderStateRecord.setAfterOrderState(afterOrderState);
                orderStateRecord.setAfterStateDesc(OrderEnum.State.getByCode(afterOrderState).getInfo());
                orderStateRecord.setUserId(spuOrder.getUserId());
                orderStateRecord.setOperatorId(operatorId);
                orderStateRecord.setOperatorRoleId(operatorRoleId.getCode());
                orderStateRecord.setRoleDesc(operatorRoleId.getValue());
                orderStateRecord.setOperateTime(LocalDateTime.now());
                orderStateRecord.setCreateTime(LocalDateTime.now());
                orderStateRecord.setUpdateTime(LocalDateTime.now());
                MQUtil.send(MQ_TAG, orderStateRecord);
                log.info("订单状态记录消息发送成功，orderStateRecordRPC: {}", orderStateRecord);
            });
        } catch (Exception e) {
            log.error("订单状态记录消息发送失败，spuOrderList: {}", spuOrderList, e);
        }

    }
}
