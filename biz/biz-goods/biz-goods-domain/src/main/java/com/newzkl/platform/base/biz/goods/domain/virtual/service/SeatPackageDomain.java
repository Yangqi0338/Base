package com.newzkl.platform.base.biz.goods.domain.virtual.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.req.virtual.SeatPackageCreateReq;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtual.SeatPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.virtual.SeatPackageUpdateReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.virtual.SeatPackageRes;

/**
 * 席位套餐
 *
 * <p>虚拟商品的一种: 平台维护套餐目录, 渠道商/供应商购买后由 biz-finance 转成钱包商品位额度</p>
 */
public interface SeatPackageDomain {

    /**
     * 新增席位套餐
     *
     * @param req 新增入参
     */
    void create(SeatPackageCreateReq req);

    /**
     * 修改席位套餐
     *
     * @param req 修改入参
     */
    void update(SeatPackageUpdateReq req);

    /**
     * 席位套餐分页
     *
     * @param req 分页查询入参
     * @return 席位套餐分页
     */
    Page<SeatPackageRes> seatPackagePage(SeatPackageQuery req);

    /**
     * 按 id 查席位套餐详情
     *
     * @param id 席位套餐 id
     * @return 套餐详情; 不存在时返回 null
     */
    SeatPackageRes detail(Long id);
}
