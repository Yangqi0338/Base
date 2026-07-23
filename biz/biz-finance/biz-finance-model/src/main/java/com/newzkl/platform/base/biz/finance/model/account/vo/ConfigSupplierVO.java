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
}
