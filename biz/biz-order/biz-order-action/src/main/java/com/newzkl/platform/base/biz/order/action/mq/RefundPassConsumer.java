package com.newzkl.platform.base.biz.order.action.mq;

import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.order.model.support.api.order.RefundPassEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;

import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 售后通过
 * @author fang
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.REFUND_PASS_MESSAGE, tag = MQ.Tag.REFUND_PASS)
public class RefundPassConsumer extends AbstractMessageMQPushConsumer<RefundPassEvent> {

    @Override
    public void remoteProcess(RefundPassEvent message, Map<String, Object> extMap) {
        log.info("售后通过消费: " + JSONObject.toJSONString(message));
        // TODO 统计
//        goodsCountFacade.spuRefundCount(new ArrayList<>(spuSaleCountAddDTOMap.values()));
        // TODO 统计
//        userCountFacade.refundCount(refundCountReqList);
        // 修改待分润记录的结算状态
        // NOTE 应该还要判断非三方订单
    }
}
