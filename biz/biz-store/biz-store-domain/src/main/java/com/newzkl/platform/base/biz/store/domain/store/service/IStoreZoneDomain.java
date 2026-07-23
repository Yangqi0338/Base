package com.newzkl.platform.base.biz.store.domain.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZonePageReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreZoneResponse;

/**
 * 门店专区
 */
public interface IStoreZoneDomain {

    Page<StoreZoneResponse> storeZonePage(StoreZonePageReq req);

    void create(StoreZoneCreateReq req);

    void update(StoreZoneUpdateReq req);

    /**
     * 新增商品个数
     */
    void increaseGoodsNum(String storeZoneCode, Integer increaseNum);

    /**
     * 新增订单个数
     */
    void increaseOrderNum(String storeZoneCode, Integer increaseNum);

}
