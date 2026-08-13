package com.newzkl.platform.base.biz.order.action.mq;

import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.ddd.facade.GoodsPaySuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 商品订单支付成功消费者
 *
 * <p>合并原 scm-message 的 MemberPaySuccess / StoreAccountPay / SkuOrderEarnings 三消费者的下游动作,
 * 区别于 {@code PaySuccessConsumer}(支付渠道回调): 本消费者处理商品订单支付成功后的编排链</p>
 *
 * @author KC
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.GOODS_ORDER_PAY_SUCCESS_MESSAGE, tag = MQ.Tag.GOODS_ORDER_PAY_SUCCESS)
public class OrderPaySuccessConsumer extends AbstractMessageMQPushConsumer<GoodsPaySuccessEvent> {

    @Autowired
    private OrderService orderService;

    /**
     * 处理商品订单支付成功事件
     *
     * @param message 商品支付成功事件
     * @param extMap  消息扩展参数
     */
    @Override
    public void remoteProcess(GoodsPaySuccessEvent message, Map<String, Object> extMap) {
        Long orderId = message.getOrderId();
        log.info("商品订单支付成功消费, 订单ID: {}", orderId);

        // 1. 渠道商余额支付编排(扣库存/扣采购金/外部供应链下单)
        orderService.orderBalancePay(orderId);

        // 2. C 端会员支付成功编排
        orderService.memberPaySuccess(orderId);

        // 3. 门店客户支付事件 —— 待接线: biz-order → biz-store 支付出站端口未建, 能力齐后补(deferred)
        log.warn("storeAccountPayEvent 待接线(biz-order→store port 未建), 订单ID: {}", orderId);

        // 4. 订单金额作为流水触发账户升级 —— 待接线: levelUp 能力缺(AccountLevelUpReq 在 biz-user, biz-account levelUp 注释未恢复)(deferred)
        log.warn("levelUp 待接线(账户升级能力缺), 订单ID: {}", orderId);
    }
}
