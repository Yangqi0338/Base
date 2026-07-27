package com.newzkl.platform.base.biz.order.domain.adapt.api;

/**
 * 收货地址出站端口
 *
 * @author KC
 */
public interface AccountShipAddressApi {

    /**
     * 查询账户名下的收货地址
     *
     * @param shipAddressId 收货地址ID
     * @param accountId     账户ID
     * @return 收货地址, 无则 null
     */
    ShipAddressDTO getAddressDetail(Long shipAddressId, Long accountId);
}
