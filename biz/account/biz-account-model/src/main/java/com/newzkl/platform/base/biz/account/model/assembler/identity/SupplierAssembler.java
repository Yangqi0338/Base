package com.newzkl.platform.base.biz.account.model.assembler.identity;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.SupplierAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 供应商
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface SupplierAssembler extends BaseAssembler<SupplierReq, SupplierVO> {

    @Override
    @Mappings({
            @Mapping(target = "receiveAddress", ignore = true)
    })
    SupplierVO req2VO(SupplierReq req);

    SupplierRes accountVO2Res(SupplierAccountVO it);
}
