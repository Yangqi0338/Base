package com.newzkl.platform.base.biz.goods.application.goods.service.virtual;

import com.newzkl.platform.base.biz.goods.model.goods.res.virtual.SeatPackageChannelRes;

/**
 * 席位套餐查询服务
 *
 * @author muc_fang
 */
public interface SeatPackageService {

    /**
     * 渠道商的席位套餐详情
     *
     * @param accountId 渠道商账号 id
     * @return 席位套餐 + 商品位额度汇总 + 字典配置价
     */
    SeatPackageChannelRes seatPackageStoreVO(Long accountId);
}
