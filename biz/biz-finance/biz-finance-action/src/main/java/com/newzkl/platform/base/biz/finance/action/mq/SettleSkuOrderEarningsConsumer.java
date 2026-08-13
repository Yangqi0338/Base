package com.newzkl.platform.base.biz.finance.action.mq;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.EarningApi;
import com.newzkl.platform.base.biz.finance.model.event.SkuOrderWaitEarningVO;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * SKU订单分润结算消费者
 *
 * <p>迁移自旧 scm-message {@code SettleSkuOrderEarningsConsumer}, 消费 SKU 订单待分润消息后结算分润</p>
 *
 * @author KC
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.EARNING_MESSAGE, tag = MQ.Tag.EARNING)
public class SettleSkuOrderEarningsConsumer extends AbstractMessageMQPushConsumer<SkuOrderWaitEarningVO> {

    @Autowired
    private EarningApi earningApi;

    /**
     * 处理SKU订单分润结算事件
     *
     * @param message SKU订单待分润数据
     * @param extMap  消息扩展参数
     */
    @Override
    public void remoteProcess(SkuOrderWaitEarningVO message, Map<String, Object> extMap) {
        log.info("SKU订单分润结算消费, skuOrderId: {}", message.getSkuOrderId());
        earningApi.settleEarning(message);
    }
}
