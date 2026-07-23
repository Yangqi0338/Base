package com.newzkl.platform.base.biz.finance.model.purse.res;

import lombok.Data;

/**
 * @author niu
 * @description: 累计供应商结算数据
 * @date 2024/7/11 18:11
 */
@Data
public class TotalSupplierSettleDataRes {

    /**
     * 累计结算
     */
    private Integer totalSettle;

    /**
     * 售后结算
     */
    private Integer sellAfter;
}
