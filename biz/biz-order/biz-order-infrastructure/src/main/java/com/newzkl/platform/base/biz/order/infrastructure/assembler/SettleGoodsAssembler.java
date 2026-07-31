package com.newzkl.platform.base.biz.order.infrastructure.assembler;

import com.newzkl.platform.base.biz.order.infrastructure.entity.SettleGoodsDO;
import com.newzkl.platform.base.biz.order.model.dto.SettleGoods;
import org.mapstruct.Mapper;

/**
* 结算商品信息表
* @author fang
*/
@Mapper(componentModel = "spring", uses = SettleGoodsConvert.class)
public interface SettleGoodsAssembler {
    /**
     * DO转Domain
     * @param settleGoodsDO
     * @return
    */
    SettleGoods doToDomain(SettleGoodsDO settleGoodsDO);
    /**
     * Domain转DO
     * @param settleGoods
     * @return
     */
    SettleGoodsDO domainToDO(SettleGoods settleGoods);
}
