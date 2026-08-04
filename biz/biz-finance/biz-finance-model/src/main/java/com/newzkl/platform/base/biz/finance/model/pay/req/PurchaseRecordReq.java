package com.newzkl.platform.base.biz.finance.model.pay.req;

import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import lombok.Data;


/**
 * 购买记录 #pay(PurchaseRecord)DTO类
 *
 * @author kc
 * @since 2025-11-25 17:24:24
 */
@Data
public class PurchaseRecordReq extends BaseRes {

    /**
     * 购买单号
     */
    private String purchaseNo;

    /**
     * 购买类型
     */
    private Integer type;

    /**
     * 交易单号
     */
    private Long tradeNo;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 支付金额
     */
    private Money payAmount;

    /**
     * 商品金额
     */
    private Money goodsAmount;

    /**
     * 支付方式
     */
    private OrderEnum.PayType payType;

    /**
     * 支付状态
     */
    private OrderEnum.State payState;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 订单信息
     */
    private String orderInfo;

}

