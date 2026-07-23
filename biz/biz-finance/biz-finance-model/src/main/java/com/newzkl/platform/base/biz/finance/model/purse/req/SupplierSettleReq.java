package com.newzkl.platform.base.biz.finance.model.purse.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 供应商结算请求
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
    private Integer settleAmount;

    /**
     * 关联结算单号
     */
    private Long joinSettleOrderNo;
}
