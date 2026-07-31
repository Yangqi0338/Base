package com.newzkl.platform.base.biz.order.domain.adapt.api;

/**
 * 余额支付出站端口
 *
 * <p>迁移: 原 domain 直连 {@code @DubboReference IBalancePayApi} 违依赖硬线,
 * 抽为出站端口, 由 infra 实现(远程调 finance 域做渠道商结算/售后退款)
 *
 * @author KC
 */
public interface BalancePayApi {

    /**
     * 渠道商结算
     *
     * <p>订单完成后将订单收益转入渠道商收益账户
     *
     * @param req 结算请求(客户/金额/关联结算单)
     */
    void channelSettle(ChannelSettleReq req);

    /**
     * 售后退款
     *
     * <p>渠道商取消未支付订单时发起原路退款
     *
     * @param req 退款请求(订单单号/售后单号)
     * @return 退款结果, 含退款警告信息
     */
    MemberRefundRes sellAfterRefund(SellAfterRefundReq req);
}
