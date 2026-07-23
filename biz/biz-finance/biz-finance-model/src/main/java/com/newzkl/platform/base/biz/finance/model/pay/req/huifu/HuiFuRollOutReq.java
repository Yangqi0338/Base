package com.newzkl.platform.base.biz.finance.model.pay.req.huifu;

import lombok.Data;

/**
 * 汇付转账请求
 *
 * @author niu
 * @description:
 * @date 2025-08-25 17:26:49
 */
@Data
public class HuiFuRollOutReq {

    /**
     * 申请金额
     */
    private Integer applyAmount;

    /**
     * 接受方汇付ID
     */
    private String huifuId;

    /**
     * 转账id
     */
    private Long rollOutId;

}