package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import org.mapstruct.Mapper;

/**
* 发货单
* @author fang
*/
@Mapper(componentModel = "spring", uses = DeliverConvert.class)
public interface DeliverAssembler {
    /**
     * DO转Domain
     * @param deliverDO
     * @return
    */
    Deliver doToDomain(DeliverDO deliverDO);
    /**
     * Domain转DO
     * @param deliver
     * @return
     */
    DeliverDO domainToDO(Deliver deliver);
}
