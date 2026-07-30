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
 * 供应商装配器
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

    /**
     * 供应商联表视图转分页出参
     *
     * @param it 供应商联表视图
     * @return 供应商分页出参
     */
    @Mappings({
            @Mapping(target = "roleId", expression = "java(it.getRole() == null ? null : it.getRole().getCode())")
    })
    SupplierRes accountVO2Res(SupplierAccountVO it);
}
