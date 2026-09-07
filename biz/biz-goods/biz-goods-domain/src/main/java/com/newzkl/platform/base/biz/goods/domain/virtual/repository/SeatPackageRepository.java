package com.newzkl.platform.base.biz.goods.domain.virtual.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.virtual.SeatPackage;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtual.SeatPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.virtual.SeatPackageRes;

import java.util.List;

/**
 * 席位套餐仓储接口
 */
public interface SeatPackageRepository {

    /**
     * 落库新增
     *
     * @param seatPackage 席位套餐
     */
    void create(SeatPackage seatPackage);

    /**
     * 按主键更新
     *
     * @param seatPackage 席位套餐
     */
    void update(SeatPackage seatPackage);

    /**
     * 按主键查详情
     *
     * @param id 主键
     * @return 套餐详情; 不存在时返回 null
     */
    SeatPackageRes detail(Long id);

    /**
     * 分页查询
     *
     * @param req 查询入参
     * @return 席位套餐分页
     */
    Page<SeatPackageRes> seatPackagePage(SeatPackageQuery req);

    /**
     * 列表查询
     *
     * @param req 查询入参
     * @return 席位套餐列表
     */
    List<SeatPackageRes> seatPackageList(SeatPackageQuery req);
}
