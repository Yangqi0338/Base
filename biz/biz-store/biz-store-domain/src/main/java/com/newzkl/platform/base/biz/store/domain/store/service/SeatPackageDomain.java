package com.newzkl.platform.base.biz.store.domain.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackagePageReq;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageResponse;

/**
 * 席位套餐
 */
public interface SeatPackageDomain {

    void create(SeatPackageCreateReq req);


    void update(SeatPackageUpdateReq req);


    Page<SeatPackageResponse> seatPackagePage(SeatPackagePageReq req);

    /**
     * 按 id 查席位套餐详情
     *
     * @param id 席位套餐 id
     * @return 套餐详情; 不存在时返回 null
     */
    SeatPackageResponse detail(Long id);

    /**
     * 新增席位个数
     */
    void increaseSeatNum(String seatPackageCode, Integer increaseNum);
}
