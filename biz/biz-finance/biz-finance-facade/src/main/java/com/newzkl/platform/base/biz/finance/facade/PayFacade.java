package com.newzkl.platform.base.biz.finance.facade;

import com.newzkl.platform.base.common.ddd.facade.SupplierSettleReq;
import com.newzkl.platform.base.common.ddd.facade.ChannelSettleReq;
import com.newzkl.platform.base.common.ddd.facade.BalancePayReq;
import com.newzkl.platform.base.common.ddd.facade.BalancePayResult;
import com.newzkl.platform.base.common.ddd.facade.MemberRefundRes;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.common.ddd.facade.SellAfterRefundReq;

/**
 * 余额支付对外契约 (facade 自带 model, 防腐: 不暴露内部 model)。
 *
 * @author niu
 * @date 2024/1/10 10:15
 */
public interface PayFacade {

    /**
     * 订单支付
     * @param req
     * @return
     */
    PayBaseResult orderPay(OrderPayReq req);

    BalancePayResult balancePay(BalancePayReq req);

    MemberRefundRes sellAfterRefund(SellAfterRefundReq req);

    void channelSettle(ChannelSettleReq req);

    void supplierSettle(SupplierSettleReq req);
}
