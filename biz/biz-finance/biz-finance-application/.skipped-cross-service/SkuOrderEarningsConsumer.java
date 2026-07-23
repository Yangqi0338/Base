package com.zkl.scm.finance.application.event;


import cn.hutool.core.map.MapUtil;
import com.zkl.scm.finance.model.earnings.req.EarningsPaymentExecReq;
import com.zkl.scm.finance.model.earnings.vo.GoodsInfoVO;
import com.zkl.scm.finance.model.event.SkuOrderEarningsEvent;
import com.zkl.scm.finance.rpc.facade.earnings.EarningFacade;
import com.zkl.scm.infrastructure.mq.annotation.MQConsumer;
import com.zkl.scm.infrastructure.mq.base.consumer.AbstractMessageMQPushConsumer;
import com.zkl.scm.model.constants.common.MQ;
import com.zkl.scm.model.enums.finance.EarningsEnum;
import com.zkl.scm.user.model.relation.req.AccountLevelUpReq;
import com.zkl.scm.user.rpc.facade.IAccountFacade;
import com.zkl.scm.util.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;

import java.util.Map;


/**
 * SKU订单:分润消息
 *
 * @author fang
 */
@Slf4j
@MQConsumer(topic = MQ.Scm_Main, consumerGroup = MQ.Tag.SKU_ORDER_EARNINGS_MESSAGE, tag = MQ.Tag.SKU_ORDER_EARNINGS)
@RequiredArgsConstructor
public class SkuOrderEarningsConsumer extends AbstractMessageMQPushConsumer<SkuOrderEarningsEvent> {

    @DubboReference
    private IAccountFacade accountFacade;
    private final EarningFacade earningFacade;

    private static EarningsPaymentExecReq getEarningsPaymentExecReq(SkuOrderEarningsEvent message) {
        EarningsPaymentExecReq earningsExecReq = TransferUtils.transfer(message, EarningsPaymentExecReq::new);

        GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
        goodsInfoVO.setGoodsName(message.getSpuName());
        goodsInfoVO.setImg(message.getSkuImg());
        goodsInfoVO.setSkuName(message.getSkuName());
        goodsInfoVO.setNum(message.getCount());
        goodsInfoVO.setAmount(message.getGoodsAmount());
        earningsExecReq.setGoodsInfoVO(goodsInfoVO);

        earningsExecReq.setOrderNo(message.getId());
        earningsExecReq.setConsumeType(EarningsEnum.ConsumeType.GOODS);
        return earningsExecReq;
    }

    @Override
    public void remoteProcess(SkuOrderEarningsEvent message, Map<String, Object> extMap) {
        EarningsPaymentExecReq earningsExecReq = getEarningsPaymentExecReq(message);

        earningFacade.doEarning(earningsExecReq);

        // 分完润后, 执行账户升级逻辑
        AccountLevelUpReq accountLevelUpReq = new AccountLevelUpReq();
        accountLevelUpReq.setAccountId(earningsExecReq.getInvitedId());
        // 订单金额作为流水进行判断
        accountLevelUpReq.setAmountScopeMap(MapUtil.of(null, message.getGoodsAmount()));
        accountFacade.levelUp(accountLevelUpReq);
    }


}
