package com.newzkl.platform.base.biz.finance.facade.model.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * 供应商购买商品位请求 (facade 自带 model, 防腐)。
 *
 * @author KC
 */
@Data
public class SupplierPurchaseGoodsSeatReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 购买数量
     */
    private Integer purchaseNum;
}
