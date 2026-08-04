package com.newzkl.platform.base.biz.order.model.support.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 供应商售后收货信息 (跨域 ACL)
 *
 * <p>供应商域 {@code AccountSupplierApi#supplierRefundVO} 出参本地降级副本, 隔离
 * biz-account 模型。仅承接售后单展示所需的收货地址。</p>
 *
 * @author KC
 */
@Data
public class SupplierRefundVO implements Serializable {

    /**
     * 供应商账户 ID
     */
    private Long id;

    /**
     * 收货地址
     */
    private ReceiveAddressOutVO receiveAddressVO;
}
