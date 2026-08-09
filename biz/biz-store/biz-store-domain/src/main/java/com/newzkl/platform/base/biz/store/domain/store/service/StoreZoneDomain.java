package com.newzkl.platform.base.biz.store.domain.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneCreateReq;
import com.newzkl.platform.base.biz.store.model.store.query.StoreZoneQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreZoneRes;

/**
 * 门店专区
 */
public interface StoreZoneDomain {

    Page<StoreZoneRes> storeZonePage(StoreZoneQuery req);

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
