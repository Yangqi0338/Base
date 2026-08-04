package com.newzkl.platform.base.biz.account.application.provider;


import com.newzkl.platform.base.biz.account.facade.IShipAddressFacade;
import com.newzkl.platform.base.biz.account.facade.model.ShipAddressOutVO;
import com.newzkl.platform.base.biz.account.model.address.vo.ShipAddressVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.Setter;
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
public class ShipAddressFacadeImpl implements IShipAddressFacade {

//    @Autowired
//    private IShipAddressRepository shipAddressRepository;

    @Override
    public ShipAddressOutVO shipAddress(Long shipAddressId) {
//        ShipAddressVO shipAddressVO = shipAddressRepository.shipAddressVO(shipAddressId);
        return TransferUtils.transfer(null, ShipAddressOutVO::new);
    }
}
