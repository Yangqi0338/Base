package com.newzkl.platform.base.biz.order.infrastructure.assembler;

import com.newzkl.platform.base.biz.order.infrastructure.entity.SpuOrderDO;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrder;
import org.mapstruct.Mapper;

/**
* SPU订单
* @author fang
*/
@Mapper(componentModel = "spring", uses = SpuOrderConvert.class)
public interface SpuOrderAssembler {
    /**
     * DO转Domain
     * @param spuOrderDO
     * @return
    */
    SpuOrder doToDomain(SpuOrderDO spuOrderDO);
    /**
     * Domain转DO
     * @param spuOrder
     * @return
     */
    SpuOrderDO domainToDO(SpuOrder spuOrder);
}
