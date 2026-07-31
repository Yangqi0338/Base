package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleOrderWaitDO;
import com.newzkl.platform.base.biz.order.model.dto.SettleOrderWait;
import com.newzkl.platform.base.biz.order.model.vo.SettleOrderWaitVO;
import org.mapstruct.Mapper;

/**
* 待结算订单信息表
* @author fang
*/
@Mapper(componentModel = "spring", uses = SettleOrderWaitConvert.class)
public interface SettleOrderWaitAssembler {
    /**
     * DO转Domain
     * @param settleOrderWaitDO
     * @return
    */
    SettleOrderWait doToDomain(SettleOrderWaitDO settleOrderWaitDO);
    SettleOrderWaitVO doToVO(SettleOrderWaitDO settleOrderWaitDO);
    /**
     * Domain转DO
     * @param settleOrderWait
     * @return
     */
    SettleOrderWaitDO domainToDO(SettleOrderWait settleOrderWait);
}
