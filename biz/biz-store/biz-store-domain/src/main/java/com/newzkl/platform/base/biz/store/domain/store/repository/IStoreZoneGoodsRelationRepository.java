package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.newzkl.platform.base.biz.store.model.store.entity.StoreZoneGoodsRelation;

import java.util.List;

/**
 * 门店专区商品关系仓储接口
 */
public interface IStoreZoneGoodsRelationRepository {
    void createBatchRelation(List<StoreZoneGoodsRelation> relationList);

}