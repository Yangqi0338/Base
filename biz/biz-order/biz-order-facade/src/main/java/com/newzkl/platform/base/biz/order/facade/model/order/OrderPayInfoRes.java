package com.newzkl.platform.base.biz.order.facade.model.order;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

// Base biz-order, 供 plugin-bi 回查(与 TradeOrderInfoRes 同源但精简)
@Data
public class OrderPayInfoRes {
    /** 订单ID */
    private Long orderId;
    /** 支付金额(元) */
    private Money payAmount;
    /** 门店ID */
    private Long storeId;
    /** 商品ID */
    private Long goodsId;
}