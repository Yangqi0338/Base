package com.newzkl.platform.base.biz.order.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 售后退款结果
 *
 * <p>迁移: 原跨域 {@code com.zkl.scm.finance.rpc.model.res.MemberRefundRes} 降级为
 * order 本地 ACL DTO, {@link BalancePayApi} 售后退款出站返回, 供下单侧读退款警告
 *
 * @author KC
 */
@Data
public class MemberRefundRes implements Serializable {

    /**
     * 退款警告信息
     */
    private String refundWarnMsg;

    /**
     * 退款单号
     */
    private Long refundNo;

    /**
     * 三方退款单号
     */
    private String thirdTradeNo;
}
