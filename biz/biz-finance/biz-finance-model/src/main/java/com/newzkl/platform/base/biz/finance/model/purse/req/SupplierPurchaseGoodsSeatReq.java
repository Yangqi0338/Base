package com.newzkl.platform.base.biz.finance.model.purse.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 供应商购买商品位请求
 *
 * @author niu
 * @date 2025-08-26 15:44:24
 */
@Data
public class SupplierPurchaseGoodsSeatReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 供应商ID */
    private Long supplierId;

    /**
     * 购买数量
     */
    private Integer purchaseNum;
}
