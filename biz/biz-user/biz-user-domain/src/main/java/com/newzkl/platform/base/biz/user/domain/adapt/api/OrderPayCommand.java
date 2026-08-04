package com.newzkl.platform.base.biz.user.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单支付出站入参
 *
 * <p>字段迁自旧 {@code com.zkl.scm.finance.rpc.model.req.OrderPayReq} 被 set 的字段。</p>
 *
 * @author KC
 */
@Data
public class OrderPayCommand implements Serializable {

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 消费类型编码（对应 {@code EarningsEnum.ConsumeType} 的 code）
     */
    private Integer consumeType;

    /**
     * 订单金额（分）
     */
    private Integer orderAmount;

    /**
     * 实付金额（分）
     */
    private Integer payAmount;

    /**
     * 订单描述
     */
    private String orderInfo;

    /**
     * 商品描述
     */
    private String goodsInfo;

    /**
     * 下单账号ID
     */
    private Long accountId;

    /**
     * 账号手机号
     */
    private Long accountMobile;

    /**
     * 注册时间（yyyyMMddHHmmss）
     */
    private String registerTime;

    /**
     * 账号名
     */
    private String accountName;

    /**
     * 支付方式 1-微信支付 2-支付宝支付
     */
    private Integer payType;
}
