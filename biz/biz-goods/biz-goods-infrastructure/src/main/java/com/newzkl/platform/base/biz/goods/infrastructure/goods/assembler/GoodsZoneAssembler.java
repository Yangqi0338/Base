package com.newzkl.platform.base.biz.goods.infrastructure.goods.assembler;

import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodsZoneDO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZone;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;

/**
 * 商品分组转换器（DO ↔ 领域实体）
 * @author sijiwang
 */
@Mapper(componentModel = "spring", uses = BaseConvert.class)
public interface GoodsZoneAssembler {

    // DO → 领域实体
    GoodsZone goodsZoneConvert(GoodsZoneDO goodsZoneDO);

    // 领域实体 → DO
    GoodsZoneDO goodsZoneDOConvert(GoodsZone goodsZone);
}