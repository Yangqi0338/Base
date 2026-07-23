package com.newzkl.platform.base.biz.finance.model.pay.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentVO extends BaseRes {

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
     * 消费类型
     */
    private Integer consumeType;

    /**
     * 支付状态 ，0，待支付，1，支付成功，2，支付失败
     */
    private Integer payState;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 订单信息
     */
    private String orderInfo;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;
}
