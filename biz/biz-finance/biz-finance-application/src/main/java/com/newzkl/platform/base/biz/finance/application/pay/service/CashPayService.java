package com.newzkl.platform.base.biz.finance.application.pay.service;

/**
 * 现金支付编排接口。
 *
 * @author niu
 */
public interface CashPayService {

    /**
     * 更新支付状态。
     *
     * @param tradeNo      交易号
     * @param thirdOrderNo 三方订单号
     */
    void alterPayState(Long tradeNo, String thirdOrderNo);
}
