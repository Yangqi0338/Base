package com.newzkl.platform.base.biz.finance.domain.adapt.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    String saveOrderPayRecord(PaymentVO req, List<OrderPayeeInfoVO> payeeInfos);

    /**
     * 更新支付状态
     * @param tradeNo
     * @param tripartiteTradeNo
     * @return
     */
    boolean alterPayState(String tradeNo,String tripartiteTradeNo);

    /**
     * 回填三方交易号
     *
     * <p>三方支付受理成功后才拿到三方单号, 需回写到已落库的支付单上。</p>
     *
     * @param tradeNo           支付单号
     * @param tripartiteTradeNo 三方交易号
     */
    void resetTripartiteTradeNo(String tradeNo, String tripartiteTradeNo);

    /**
     * 交易单信息查询
     * @param tradeNo
     * @return
     */
    TradeOrderInfoRes tradeOrderQuery(String tradeNo);

    /**
     * 按订单号查询交易单信息
     * @param orderNo 订单号
     * @return 交易单信息
     */
    TradeOrderInfoRes tradeOrderQueryByOrderNo(Long orderNo);

    /**
     * 交易单信息分页查询
     *
     * @param query 充值记录查询
     * @return 支付记录分页
     */
    Page<PaymentVO> tradeOrderQuery(PaymentQuery query);
}
