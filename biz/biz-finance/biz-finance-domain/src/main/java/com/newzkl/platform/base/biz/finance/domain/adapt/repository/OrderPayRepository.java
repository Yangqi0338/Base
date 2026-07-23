package com.newzkl.platform.base.biz.finance.domain.adapt.repository;


import com.newzkl.platform.base.biz.finance.model.pay.req.PaymentQuery;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.pay.vo.OrderPayeeInfoVO;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PaymentVO;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2023/12/19 17:01
 */
public interface OrderPayRepository {

    /**
     * 保存订单支付记录
     * @param req
     * @param payeeInfos
     * @return
     */
    Long saveOrderPayRecord(PaymentVO req, List<OrderPayeeInfoVO> payeeInfos);

    /**
     * 更新支付状态
     * @param tradeNo
     * @param tripartiteTradeNo
     * @return
     */
    boolean alterPayState(Long tradeNo,String tripartiteTradeNo);

    /**
     * 交易单信息查询
     * @param tradeNo
     * @return
     */
    TradeOrderInfoRes tradeOrderQuery(Long tradeNo);

    /**
     * 交易单信息列表查询
     *
     * @param query
     * @return
     */
    List<PaymentVO> tradeOrderQuery(PaymentQuery query);
}
