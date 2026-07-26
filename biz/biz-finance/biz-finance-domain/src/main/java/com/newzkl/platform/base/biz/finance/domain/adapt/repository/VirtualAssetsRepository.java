package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRes;

import java.util.List;

/**
 * 虚拟资产 (virtual_assets) 存储接口。
 *
 * @author KC
 */
public interface VirtualAssetsRepository {

    /**
     * 查询虚拟资产分页列表。
     *
     * @param query 查询条件
     * @return 虚拟资产列表, 无数据返回空集合
     */
    List<VirtualAssetsRes> queryPage(VirtualAssetsQuery query);
}
