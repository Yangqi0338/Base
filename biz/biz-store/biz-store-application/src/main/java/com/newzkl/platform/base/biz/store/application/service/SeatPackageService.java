package com.newzkl.platform.base.biz.store.application.service;

import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageChannelRes;

/**
 * 席位套餐查询服务
 *
 * @author muc_fang
 */
public interface SeatPackageService {

    /**
     * 渠道商的席位套餐详情
     */
    SeatPackageChannelRes seatPackageStoreVO(Long accountId);
}
