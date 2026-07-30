package com.newzkl.platform.base.biz.finance.domain.virtual.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsRecordQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRecordRes;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRes;

import java.util.List;

/**
 * 虚拟资产领域服务
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.domain.virtual.service.IVirtualAssetsService}
 * 的两个查询能力 (写入能力由 Dubbo 侧驱动, 不在本次 action 迁移范围)。</p>
 *
 * @author KC
 */
public interface VirtualAssetsDomain {

    /**
     * 查询虚拟资产
     *
     * @param query 查询条件
     * @return 虚拟资产列表, 无数据返回空集合
     */
    List<VirtualAssetsRes> queryVirtualAssets(VirtualAssetsQuery query);

    /**
     * 查询虚拟资产变动记录分页
     *
     * @param query 查询条件
     * @return 变动记录分页
     */
    Page<VirtualAssetsRecordRes> queryVirtualAssetsRecord(VirtualAssetsRecordQuery query);
}
