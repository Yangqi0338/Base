package com.newzkl.platform.base.biz.finance.domain.adapt.api;

/**
 * 消息发送出站端口 (outbound port)
 *
 * <p>迁移: 原 scm-message 直连 MQ 发送; 中台化后 domain 只依赖端口, 由 infra 经 core-mq 实现</p>
 *
 * @author KC
 */
public interface MessageApi {

    /**
     * 发送商品订单支付成功消息
     *
     * <p>触发 biz-order 侧 {@code OrderPaySuccessConsumer} 的异步编排链
     * (渠道商余额支付/会员支付成功/门店客户支付/账户升级)</p>
     *
     * @param orderId 商品订单ID
     */
    void sendGoodsPaySuccess(Long orderId);
}
