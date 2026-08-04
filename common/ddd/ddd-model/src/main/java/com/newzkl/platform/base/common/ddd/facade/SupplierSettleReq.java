package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商结算请求 (facade 自带 model, 防腐: 金额 Integer 分)。
 *
 * @author KC
 */
@Data
public class SupplierSettleReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 结算金额 (分)
     */
    private Money settleAmount;

    /**
     * 关联结算单号
     */
    private Long joinSettleOrderNo;
}
