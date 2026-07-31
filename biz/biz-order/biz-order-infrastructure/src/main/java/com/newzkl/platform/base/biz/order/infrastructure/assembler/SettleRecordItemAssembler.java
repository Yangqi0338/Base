package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordItemDO;
import com.newzkl.platform.base.biz.order.model.dto.SettleRecordItem;
import org.mapstruct.Mapper;

/**
* 结算记录明细表
* @author fang
*/
@Mapper(componentModel = "spring", uses = SettleRecordItemConvert.class)
public interface SettleRecordItemAssembler {
    /**
     * DO转Domain
     * @param settleRecordItemDO
     * @return
    */
    SettleRecordItem doToDomain(SettleRecordItemDO settleRecordItemDO);
    /**
     * Domain转DO
     * @param settleRecordItem
     * @return
     */
    SettleRecordItemDO domainToDO(SettleRecordItem settleRecordItem);
}
