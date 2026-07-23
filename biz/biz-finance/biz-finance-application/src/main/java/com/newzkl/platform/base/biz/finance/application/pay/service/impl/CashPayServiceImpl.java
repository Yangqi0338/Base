package com.newzkl.platform.base.biz.finance.application.pay.service.impl;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.application.pay.service.PurchaseRecordService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.model.event.PaySuccessEvent;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.common.core.rocketmq.utils.MQUtil;
import com.newzkl.platform.base.biz.finance.model.support.MQ;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 现金支付编排实现。
 *
 * @author niu
 */
@Service
@RequiredArgsConstructor
public class CashPayServiceImpl implements CashPayService {

    private final OrderPayDomain orderPayService;

    private final AccountPurseDomain accountPurseService;

    private final AccountPurseConfigDomain accountPurseConfigService;

    private final PurchaseRecordService purchaseRecordAction;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterPayState(Long tradeNo, String thirdOrderNo) {
        // 1、更新支付状态,flag表示是否更新成功
        boolean flag = orderPayService.alterPayState(tradeNo, thirdOrderNo);
        if (flag) {
            TradeOrderInfoRes tradeOrder = orderPayService.tradeOrderQuery(tradeNo);
            EarningsEnum.ConsumeType consumeType = tradeOrder.getConsumeType();
            switch (consumeType) {
                case RECHARGE:
                    // 2、增加账户余额 + 变动记录修改
                    AccountPurseAlterRecordReq recharge = buildAccountPurseAlterRecord(tradeOrder,
                            PurseEnum.FinanceUser.CHANNEL,
                            PurseEnum.PurseType.PURCHASE,
                            PurseEnum.PurseAlterType.RECHARGE);
                    accountPurseService.addAmount(recharge);
                    // 3、渠道商采购金充值后，更新服务费
                    accountPurseConfigService.alterChannelNowChargeConfig(tradeOrder.getAccountId(), tradeOrder.getPayAmount());
                    break;
                case SUPPLIER_RECHARGE:
                    // 2、增加收益余额 + 变动记录修改
                    AccountPurseAlterRecordReq supplierRecharge = buildAccountPurseAlterRecord(tradeOrder,
                            PurseEnum.FinanceUser.SUPPLIER,
                            PurseEnum.PurseType.MARKETING,
                            PurseEnum.PurseAlterType.SUPPLIER_OPERATOR_RECHARGE);
                    accountPurseService.addAmount(supplierRecharge);
                    break;
                case GOODS_SEAT:
                    // 供应商购买商品位只能扣采购金,所以不会到这里
                    PurchaseRecordReq saveCommand = new PurchaseRecordReq();
                    saveCommand.setPayState(OrderEnum.State.SUCCESS);
                    saveCommand.setTradeNo(tradeNo);
                    purchaseRecordAction.seatPackageSaveOrUpdate(saveCommand);
                    break;
                default:
                    // 2、其他消费支付成功后发送mq消息
                    MQUtil.send(MQ.Tag.LIANLIAN_PAY_SUCCESS, new PaySuccessEvent(tradeNo));
                    break;
            }
        }
    }

    private AccountPurseAlterRecordReq buildAccountPurseAlterRecord(TradeOrderInfoRes tradeOrder,
                                                                    PurseEnum.FinanceUser accountType,
                                                                    PurseEnum.PurseType purseType,
                                                                    PurseEnum.PurseAlterType alterType) {
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(tradeOrder.getAccountId());
        req.setPurseType(purseType);
        req.setAccountType(accountType);
        req.setAlterType(alterType);
        req.setAmount(tradeOrder.getPayAmount());
        req.setJoinRecordId(tradeOrder.getTradeNo());
        req.setRemark(JSONUtil.toJsonStr(tradeOrder));
        return req;
    }
}
