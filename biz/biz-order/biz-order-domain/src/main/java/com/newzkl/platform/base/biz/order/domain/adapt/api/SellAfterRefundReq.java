package com.newzkl.platform.base.biz.order.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 售后退款请求
 *
 * <p>迁移: 原跨域 {@code com.zkl.scm.finance.rpc.model.req.SellAfterRefundReq} 降级为
 * order 本地 ACL DTO, 经 {@link BalancePayApi} 出站发起售后退款
 *
 * @author KC
 */
@Data
public class SellAfterRefundReq implements Serializable {

    /**
     * 客户ID
     */
    private Long accountId;

    /**
     * 退款金额
     */
    private Integer refundAmount;

    /**
     * 服务费
     */
    private Integer serviceAmount;

    /**
     * 订单单号
     */
    private Long orderNo;

    /**
     * 售后单号
     */
    private Long sellAfterOrderNo;
}
