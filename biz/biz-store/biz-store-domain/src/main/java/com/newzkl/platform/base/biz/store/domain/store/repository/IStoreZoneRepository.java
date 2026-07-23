package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreZone;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZonePageReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreZoneResponse;

/**
 * 门店专区仓储接口
 */
public interface IStoreZoneRepository {
    Page<StoreZoneResponse> storeZonePage(StoreZonePageReq req);

    void create(StoreZone storeZone);

    void update(StoreZone transfer);

    /**
     * 新增商品个数
     */
    void increaseGoodsNum(String storeZoneCode, Integer increaseNum);

    /**
     * 新增订单个数
     */
    void increaseOrderNum(String storeZoneCode, Integer increaseNum);
}