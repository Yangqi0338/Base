package com.newzkl.platform.base.biz.finance.action.mq;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.OrderApi;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.PackOrderApi;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;

import com.newzkl.platform.base.biz.finance.model.event.PaySuccessEvent;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.ddd.facade.PackOrderRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 支付成功
 * @author fang
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.PAYMENT_PAY_SUCCESS_MESSAGE, tag = MQ.Tag.PAYMENT_PAY_SUCCESS)
public class PaySuccessConsumer extends AbstractMessageMQPushConsumer<PaySuccessEvent> {

    @Autowired
    private OrderPayDomain orderPayDomain;

    @Autowired
    private PackOrderApi packOrderApi;

    @Autowired
    private OrderApi orderApi;

    @Override
    public void remoteProcess(PaySuccessEvent message, Map<String, Object> extMap) {
        Long tradeNo = message.getOrderId();
        log.info("开始消费: 查询订单信息 :" + tradeNo);
        TradeOrderInfoRes tradeOrderInfoRes = orderPayDomain.tradeOrderQuery(tradeNo);
        //甄选师礼包支付成功业务
        Long orderNo = tradeOrderInfoRes.getOrderNo();
        if(EarningsEnum.ConsumeType.PICK_PACK == tradeOrderInfoRes.getConsumeType()){
            log.info("查询订单信息 :" + orderNo);
            PackOrderRpcVO packOrderRpcVO = packOrderApi.packOrderVO(orderNo);
            Long accountId = packOrderRpcVO.getAccountId();

            // 修正为账号升级判断
            log.info("账号升级判断: " + accountId + ":" + packOrderRpcVO.getPackLevel());

//            AccountLevelUpReq accountLevelUpReq = new AccountLevelUpReq();
//            accountLevelUpReq.setAccountId(accountId);
//
//            PackGoodsInfo packGoodsInfo = new PackGoodsInfo();
//            packGoodsInfo.setId(packOrderRpcVO.getPackId());
//            packGoodsInfo.setAmount(packOrderRpcVO.getAmount());
//            packGoodsInfo.setLevel(packOrderRpcVO.getPackLevel());
//            packGoodsInfo.setType(packOrderRpcVO.getPackType());
//            accountLevelUpReq.setPackInfo(packGoodsInfo);
//            accountFacade.levelUp(accountLevelUpReq);

            log.info("通知礼包订单:" + orderNo);
            packOrderApi.paySuccess(orderNo);
        }else if (EarningsEnum.ConsumeType.GOODS == tradeOrderInfoRes.getConsumeType()) {
//            orderFacade.orderMemberPay(Collections.singletonList(orderNo));
            // 后续扣减库存、扣减采购金、外部供应链订单请求创建订单放在支付后的异步处理中
            orderApi.orderChannelPay(orderNo);
        }
    }
}
