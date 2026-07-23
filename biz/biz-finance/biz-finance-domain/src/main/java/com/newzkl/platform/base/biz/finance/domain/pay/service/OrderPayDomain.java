package com.newzkl.platform.base.biz.finance.domain.pay.service;


import com.newzkl.platform.base.biz.finance.model.pay.req.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.PaymentQuery;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.pay.vo.OrderPayeeInfoVO;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PaymentVO;

import java.util.List;

/**
 * @author niu
 * @description: 订单支付接口
 * @date 2023/12/19 15:00
 */
public interface OrderPayDomain {

    /**
     * 保存订单支付记录
     *
     * @param req
     * @param orderPayeeInfos
     * @return
     */
    Long saveOrderPayRecord(OrderPayReq req, List<OrderPayeeInfoVO> orderPayeeInfos);

    /**
     * 更新支付状态
     *
     * @param tradeNo
     * @param tripartiteTradeNo
     * @return
     */
    boolean alterPayState(Long tradeNo, String tripartiteTradeNo);

    /**
     * 交易单信息查询
     *
     * @param tradeNo
     * @return
     */
    TradeOrderInfoRes tradeOrderQuery(Long tradeNo);

    List<PaymentVO> tradeOrderQuery(PaymentQuery query);
}
