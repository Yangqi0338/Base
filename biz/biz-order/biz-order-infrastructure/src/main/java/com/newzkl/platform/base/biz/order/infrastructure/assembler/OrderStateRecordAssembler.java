package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import com.newzkl.platform.base.biz.order.infrastructure.entity.OrderStateRecordDO;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateRecordEntity;
import org.mapstruct.Mapper;

/**
 * 订单状态记录转换
 * @author sijiwang
 */
@Mapper(componentModel = "spring", uses = OrderStateRecordConvert.class)
public interface OrderStateRecordAssembler {
    /**
     * 订单状态记录转换
     * @param entity
     * @return
     */
    OrderStateRecordDO domainToDO(OrderStateRecordEntity entity);

    /**
     * 订单状态记录转换
     * @param orderStateRecordDO
     * @return
     */
    OrderStateRecordEntity doToDomain(OrderStateRecordDO orderStateRecordDO);
}
