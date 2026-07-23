package com.newzkl.platform.base.biz.goods.infrastructure.goods.assembler;

import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodsZoneGoodsRelDO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZoneGoodsRel;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;

/**
 * 商品分组-商品关联转换器（DO ↔ 领域实体）
 * @author sijiwang
 */
@Mapper(componentModel = "spring", uses = BaseConvert.class)
public interface GoodsZoneGoodsRelAssembler {

    // DO → 领域实体
    GoodsZoneGoodsRel goodsZoneGoodsRelConvert(GoodsZoneGoodsRelDO relDO);

    // 领域实体 → DO
    GoodsZoneGoodsRelDO goodsZoneGoodsRelDOConvert(GoodsZoneGoodsRel rel);
}