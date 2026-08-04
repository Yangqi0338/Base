package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户退款返回 (facade 自带 model, 防腐)。
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
