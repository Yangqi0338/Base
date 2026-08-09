package com.newzkl.platform.base.biz.finance.model.pay.req.huifu;

import lombok.Data;

/**
 * 汇付提现请求
 *
 * @author niu
 * @date 2025-08-25 17:26:49
 */
@Data
public class HuiFuWithdrawReq {

    /**
     * 提现金额
     */
    private Long withdrawId;

    /**
     * 提现金额
     */
    private Integer withdrawAmount;

    /**
     * 取现卡序列
     */
    private String tokenNo;

}