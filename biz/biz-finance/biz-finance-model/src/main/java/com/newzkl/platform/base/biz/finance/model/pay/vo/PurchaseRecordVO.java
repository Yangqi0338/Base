package com.newzkl.platform.base.biz.finance.model.pay.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import lombok.Data;


/**
 * 购买记录 #pay(PurchaseRecord)DTO类
 *
 * @author kc
 * @since 2025-11-25 17:24:22
 */
@Data
public class PurchaseRecordVO extends BaseRes {

    /**
     * 购买单号
     */
    private String purchaseNo;

    /**
     * 购买类型
     */
    private PurseEnum.PurchaseRecordType type;

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
    private Integer payAmount;

    /**
     * 商品金额
     */
    private Integer goodsAmount;

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

