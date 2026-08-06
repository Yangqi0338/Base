package com.newzkl.platform.base.biz.finance.model.purse.res;

import com.newzkl.platform.base.common.core.model.money.Money;
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
    private Money totalSettle;

    /**
     * 售后结算
     */
    private Money sellAfter;
}
