package com.newzkl.platform.base.biz.finance.domain.virtual.service.impl;

import com.newzkl.platform.base.biz.finance.domain.adapt.repository.VirtualAssetsRecordRepository;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.VirtualAssetsRepository;
import com.newzkl.platform.base.biz.finance.domain.virtual.service.VirtualAssetsDomain;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsRecordQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRecordRes;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 虚拟资产领域服务实现。
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class VirtualAssetsDomainImpl implements VirtualAssetsDomain {

    private final VirtualAssetsRepository virtualAssetsRepository;

    private final VirtualAssetsRecordRepository virtualAssetsRecordRepository;

    /**
     * 查询虚拟资产。
     *
     * @param query 查询条件
     * @return 虚拟资产列表, 无数据返回空集合
     */
    @Override
    public List<VirtualAssetsRes> queryVirtualAssets(VirtualAssetsQuery query) {
        List<VirtualAssetsRes> list = virtualAssetsRepository.queryPage(query);
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 查询虚拟资产变动记录。
     *
     * @param query 查询条件
     * @return 变动记录列表, 无数据返回空集合
     */
    @Override
    public List<VirtualAssetsRecordRes> queryVirtualAssetsRecord(VirtualAssetsRecordQuery query) {
        List<VirtualAssetsRecordRes> list = virtualAssetsRecordRepository.queryPage(query);
        return list == null ? Collections.emptyList() : list;
    }
}
