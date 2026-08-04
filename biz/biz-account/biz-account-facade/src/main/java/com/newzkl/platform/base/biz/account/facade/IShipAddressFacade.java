package com.newzkl.platform.base.biz.account.facade;


import com.newzkl.platform.base.biz.account.facade.model.ShipAddressOutVO;

/**
 * @author muc_fang
 * @Description: 账号
 * @date 2023/8/310:34
 */
public interface IShipAddressFacade {

    /**
     * 获取收货地址信息
     * @param shipAddressId
     * @return
     */
    ShipAddressOutVO shipAddress(Long shipAddressId);
}
