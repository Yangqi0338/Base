package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsRecordQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRecordRes;

/**
 * 虚拟资产变动记录 (virtual_assets_record) 存储接口
 *
 * @author KC
 */
public interface VirtualAssetsRecordRepository {

    /**
     * 查询虚拟资产变动记录分页
     *
     * @param query 查询条件
     * @return 变动记录分页
     */
    Page<VirtualAssetsRecordRes> queryPage(VirtualAssetsRecordQuery query);
}
