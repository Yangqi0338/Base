package com.newzkl.platform.base.biz.order.infrastructure.assembler;

import com.zkl.scm.sale.domain.order.model.entity.Order;
import com.zkl.scm.sale.infrastructure.assembler.OrderConvert;
import com.newzkl.platform.base.biz.order.infrastructure.entityOrderDO;
import org.mapstruct.Mapper;

/**
* 订单
* @author fang
*/
@Mapper(componentModel = "spring", uses = OrderConvert.class)
public interface OrderAssembler {
    /**
     * DO转Domain
     * @param orderDO
     * @return
    */
    Order doToDomain(OrderDO orderDO);
    /**
     * Domain转DO
     * @param order
     * @return
     */
    OrderDO domainToDO(Order order);
}
