package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商结算请求
 *
 * @author niu
 * @date 2024/1/26 18:04
 */
@Data
public class SupplierSettleReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 结算金额
     */
    private Money settleAmount;

    /**
     * 关联结算单号
     */
    private Long joinSettleOrderNo;
}
