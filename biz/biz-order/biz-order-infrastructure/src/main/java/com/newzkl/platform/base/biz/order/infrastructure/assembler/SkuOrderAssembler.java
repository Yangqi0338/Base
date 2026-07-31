package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import com.newzkl.platform.base.biz.order.infrastructure.entity.SkuOrderDO;
import com.newzkl.platform.base.biz.order.model.dto.SkuOrder;
import org.mapstruct.Mapper;

/**
* SKU订单
* @author fang
*/
@Mapper(componentModel = "spring", uses = SkuOrderConvert.class)
public interface SkuOrderAssembler {
    /**
     * DO转Domain
     * @param skuOrderDO
     * @return
    */
    SkuOrder doToDomain(SkuOrderDO skuOrderDO);
    /**
     * Domain转DO
     * @param skuOrder
     * @return
     */
    SkuOrderDO domainToDO(SkuOrder skuOrder);
}
