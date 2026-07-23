package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.SeatPackage;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackagePageReq;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageResponse;

import java.util.List;

/**
 * 席位套餐仓储接口
 */
public interface SeatPackageRepository {

    void create(SeatPackage seatPackage);

    void update(SeatPackage seatPackage);

    SeatPackageResponse detail(Long id);

    Page<SeatPackageResponse> seatPackagePage(SeatPackagePageReq req);

    List<SeatPackageResponse> seatPackageList(SeatPackagePageReq req);

    /**
     * 新增席位个数
     */
    void increaseSeatNum(String seatPackageCode, Integer increaseNum);
}