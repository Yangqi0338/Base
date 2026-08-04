package com.newzkl.platform.base.biz.user.domain.adapt.api;

/**
 * 订单支付出站端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.finance.rpc.api.pay.IOrderPayApi}。
 * 礼包订单发起支付，实际支付能力归 biz-finance，本域仅声明消费端口 + 兜底实现，
 * 由入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
public interface OrderPayApi {

    /**
     * 发起订单支付
     *
     * @param command 支付入参
     * @return 支付结果
     */
    PayResultDTO orderPay(OrderPayCommand command);
}
