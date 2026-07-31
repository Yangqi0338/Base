package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleRecordDO;
import com.newzkl.platform.base.biz.order.model.dto.SettleRecord;
import com.newzkl.platform.base.biz.order.model.vo.SettleRecordVO;
import org.mapstruct.Mapper;

/**
* 结算记录表
* @author fang
*/
@Mapper(componentModel = "spring", uses = SettleRecordConvert.class)
public interface SettleRecordAssembler {
    /**
     * DO转Domain
     * @param settleRecordDO
     * @return
    */
    SettleRecord doToDomain(SettleRecordDO settleRecordDO);
    SettleRecordVO doToVO(SettleRecordDO settleRecordDO);
    /**
     * Domain转DO
     * @param settleRecord
     * @return
     */
    SettleRecordDO domainToDO(SettleRecord settleRecord);
}
