package com.newzkl.platform.base.biz.finance.domain.pay.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
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
     * 回填三方交易号
     *
     * <p>三方支付受理成功后才拿到三方单号, 需回写到已落库的支付单上。</p>
     *
     * @param tradeNo           支付单号
     * @param tripartiteTradeNo 三方交易号
     */
    void resetTripartiteTradeNo(Long tradeNo, String tripartiteTradeNo);

    /**
     * 交易单信息查询
     *
     * @param tradeNo
     * @return
     */
    TradeOrderInfoRes tradeOrderQuery(Long tradeNo);

    /**
     * 交易单信息分页查询
     *
     * @param query 充值记录查询
     * @return 支付记录分页
     */
    Page<PaymentVO> tradeOrderQuery(PaymentQuery query);
}
