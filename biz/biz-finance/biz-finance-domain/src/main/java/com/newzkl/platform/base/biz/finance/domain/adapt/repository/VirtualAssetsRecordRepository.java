package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsRecordQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRecordRes;

import java.util.List;

/**
 * 虚拟资产变动记录 (virtual_assets_record) 存储接口。
 *
 * @author KC
 */
public interface VirtualAssetsRecordRepository {

    /**
     * 查询虚拟资产变动记录分页列表。
     *
     * @param query 查询条件
     * @return 变动记录列表, 无数据返回空集合
     */
    List<VirtualAssetsRecordRes> queryPage(VirtualAssetsRecordQuery query);
}
