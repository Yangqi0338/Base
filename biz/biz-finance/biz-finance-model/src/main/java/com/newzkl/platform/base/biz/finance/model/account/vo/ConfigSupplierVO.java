package com.newzkl.platform.base.biz.finance.model.account.vo;

import lombok.Data;

/**
 * @author niu
 * @description:
 * @date 2025-08-25 12:00:53
 */
@Data
public class ConfigSupplierVO {

    /**
     * 保证金扣除比例
     */
    private Integer depositSettleSub;

    /**
     * 商品位费用
     */
    private Integer skuSpaceFee;

    /**
     * 提现手续费比例
     */
    private Integer withdrawRate;

    /**
     * 是否跳过保证金审核 (1 跳过, 提交即视为审核通过直接入账; 其余走人工审核)
     */
    private Integer skipPromiseAudit;
}
