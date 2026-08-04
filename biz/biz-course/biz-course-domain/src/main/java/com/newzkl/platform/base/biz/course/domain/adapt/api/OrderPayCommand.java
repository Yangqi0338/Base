package com.newzkl.platform.base.biz.course.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单支付出站入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.finance.rpc.model.req.OrderPayReq} 被 set 的字段。
 * 课程购买与礼包购买各自持有独立出站端口副本(跨域禁共享 model), 结构一致。</p>
 *
 * @author KC
 */
@Data
public class OrderPayCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程消费类型编码
     *
     * <p>对应源 {@code EarningsEnum.ConsumeType.COURSE} 的 code(9)。该枚举归 finance/account 域,
     * 跨域禁引, 本域以常量固化避免物理耦合。</p>
     */
    public static final Integer CONSUME_TYPE_COURSE = 9;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 消费类型编码
     */
    private Integer consumeType;

    /**
     * 订单金额(分)
     */
    private Integer orderAmount;

    /**
     * 实付金额(分)
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
     * 账号名
     */
    private String accountName;

    /**
     * 支付方式 1-微信支付 2-支付宝支付
     */
    private Integer payType;
}
