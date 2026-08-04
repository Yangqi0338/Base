package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.IShipAddressFacade;
import com.newzkl.platform.base.biz.account.facade.model.ShipAddressOutVO;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountShipAddressApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ShipAddressDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.zkl.scm.user.domain.address.model.vo.ShipAddressVO;
import com.zkl.scm.user.domain.address.repository.IShipAddressRepository;
import com.zkl.scm.user.rpc.facade.IShipAddressFacade;
import com.zkl.scm.user.rpc.model.address.ShipAddressOutVO;
import com.zkl.scm.web.utils.TransferUtils;
import lombok.Setter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/1915:49
 */
@Component
@DubboService
@Setter(onMethod_ = @Autowired)
public class ShipAddressApiImpl implements AccountShipAddressApi {

    @DubboReference
    private IShipAddressFacade shipAddressFacade;

    @Override
    public ShipAddressDTO getAddressDetail(Long shipAddressId) {
        ShipAddressOutVO shipAddressOutVO = shipAddressFacade.shipAddress(shipAddressId);
        return TransferUtils.transfer(shipAddressOutVO, ShipAddressDTO::new);
    }
}
