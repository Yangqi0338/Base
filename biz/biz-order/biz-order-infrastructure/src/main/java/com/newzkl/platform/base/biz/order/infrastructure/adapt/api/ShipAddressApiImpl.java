package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.IShipAddressFacade;
import com.newzkl.platform.base.biz.account.facade.model.ShipAddressOutVO;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountShipAddressApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ShipAddressDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/1915:49
 */
@Component("orderShipAddressApi")
public class ShipAddressApiImpl implements AccountShipAddressApi {

    @RpcReference
    private IShipAddressFacade shipAddressFacade;

    @Override
    public ShipAddressDTO getAddressDetail(Long shipAddressId) {
        ShipAddressOutVO shipAddressOutVO = shipAddressFacade.shipAddress(shipAddressId);
        return TransferUtils.transfer(shipAddressOutVO, ShipAddressDTO::new);
    }
}
