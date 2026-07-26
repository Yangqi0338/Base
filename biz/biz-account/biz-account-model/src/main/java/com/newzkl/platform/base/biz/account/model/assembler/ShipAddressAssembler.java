package com.newzkl.platform.base.biz.account.model.assembler;

import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressReq;
import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.biz.account.model.address.vo.ShipAddressVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import org.mapstruct.Mapper;

/**
 * 收货地址转换器。
 *
 * @author KC
 */
@Mapper(componentModel = "spring")
public interface ShipAddressAssembler extends BaseAssembler<ShipAddressReq, ShipAddressVO> {

    /**
     * 领域视图转对外出参。
     *
     * @param it 领域视图
     * @return 对外出参
     */
    ShipAddressRes vo2Res(ShipAddressVO it);
}
