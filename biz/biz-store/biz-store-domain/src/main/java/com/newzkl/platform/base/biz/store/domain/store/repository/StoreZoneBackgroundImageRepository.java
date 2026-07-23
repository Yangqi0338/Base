package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.newzkl.platform.base.biz.store.model.store.entity.StoreZoneBackgroundImage;

import java.util.List;

/**
 * 门店专区背景图仓储接口
 */
public interface StoreZoneBackgroundImageRepository {
    void createBatchImage(List<StoreZoneBackgroundImage> imageList);
}