package com.newzkl.platform.base.biz.order.domain.adapt.api;

/**
 * 供应商出站端口
 *
 * @author KC
 */
public interface AccountSupplierApi {

    /**
     * 查询供应商结算单据类型
     *
     * @param supplierId 供应商账户ID
     * @return 结算单据类型, 未接入外部实现时为 0
     */
    Integer settleOrderType(Long supplierId);
}
