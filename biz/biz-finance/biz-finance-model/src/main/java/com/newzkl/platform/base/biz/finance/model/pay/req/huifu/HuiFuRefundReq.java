package com.newzkl.platform.base.biz.finance.model.pay.req.huifu;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 汇付退款请求
 *
 * @author niu
 * @date 2025-08-25 17:26:49
 */
@Data
public class HuiFuRefundReq {

    /**
     * 售后单号
     */
    private Long sellAfterOrderNo;

    /**
     * 退款金额
     */
    private Integer refundAmount;

    /**
     * 原交易单号
     */
    private String tradeNo;

    /**
     * 原交易日期
     */
    private LocalDateTime tradeDate;

}
