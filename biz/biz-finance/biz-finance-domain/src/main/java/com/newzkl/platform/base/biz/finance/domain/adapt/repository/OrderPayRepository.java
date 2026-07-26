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
     * 回填三方交易号。
     *
     * <p>三方支付受理成功后才拿到三方单号, 需回写到已落库的支付单上。</p>
     *
     * @param tradeNo           支付单号
     * @param tripartiteTradeNo 三方交易号
     */
    void resetTripartiteTradeNo(Long tradeNo, String tripartiteTradeNo);

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
