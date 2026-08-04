package com.newzkl.platform.base.biz.finance.domain.pay.service.impl;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.OrderPayRepository;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.model.assembler.PaymentAssembler;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.PaymentQuery;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.pay.vo.OrderPayeeInfoVO;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PaymentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author niu
 * @description: 订单支付接口实现
 * @date 2023/12/19 17:01
 */
@Service
@RequiredArgsConstructor
public class OrderPayDomainImpl implements OrderPayDomain {

    private final OrderPayRepository orderPayRepository;
    private final PaymentAssembler assembler;

    @Override
    public Long saveOrderPayRecord(OrderPayReq req, List<OrderPayeeInfoVO> orderPayeeInfos) {
        PaymentVO paymentVO = assembler.req2VO(req);
        if (!JSONUtil.isTypeJSON(paymentVO.getOrderInfo())) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.set("value", paymentVO.getOrderInfo());
            paymentVO.setOrderInfo(JSONUtil.toJsonStr(jsonObj));
        }
        return orderPayRepository.saveOrderPayRecord(paymentVO, orderPayeeInfos);
    }

    @Override
    public boolean alterPayState(Long tradeNo, String tripartiteTradeNo) {
        return orderPayRepository.alterPayState(tradeNo, tripartiteTradeNo);
    }

    @Override
    public void resetTripartiteTradeNo(Long tradeNo, String tripartiteTradeNo) {
        orderPayRepository.resetTripartiteTradeNo(tradeNo, tripartiteTradeNo);
    }

    @Override
    public TradeOrderInfoRes tradeOrderQuery(Long tradeNo) {
        return orderPayRepository.tradeOrderQuery(tradeNo);
    }

    @Override
    public Page<PaymentVO> tradeOrderQuery(PaymentQuery query) {
        return orderPayRepository.tradeOrderQuery(query);
    }
}
