package com.newzkl.platform.base.biz.finance.application.pay.service;

import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayRes;

/**
 * 现金支付编排接口
 *
 * @author niu
 */
public interface CashPayService {

    /**
     * 更新支付状态
     *
     * @param tradeNo      交易号
     * @param thirdOrderNo 三方订单号
     */
    void alterPayState(String tradeNo, String thirdOrderNo);

    /**
     * 拉起三方现金支付
     *
     * <p>迁移自 new-scm {@code OrderPayApiImpl.orderPay}, 去掉 {@code @DubboService} 暴露,
     * 改为中台内部编排。按订单号加分布式锁 + 结果缓存做幂等, 重复拉起直接返回缓存结果。</p>
     *
     * @param req 支付入参
     * @return 汇付支付结果 (含二维码)
     */
    HuiFuPayRes orderPay(OrderPayReq req);
}
