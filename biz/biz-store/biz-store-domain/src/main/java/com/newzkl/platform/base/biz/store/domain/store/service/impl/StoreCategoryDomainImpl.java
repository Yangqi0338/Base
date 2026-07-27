package com.newzkl.platform.base.biz.store.domain.store.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.store.model.store.command.StoreCategorySaveCommand;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreCategory;
import com.newzkl.platform.base.biz.store.model.store.req.StoreCategoryQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreCategoryRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreSearchRes;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreCategoryRepository;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreCategoryDomain;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门店分类 (StoreCategory)存储实现
 *
 * @author kc
 * @since 2026-01-27 11:21:03
 */
@Service
@RequiredArgsConstructor
public class StoreCategoryDomainImpl implements StoreCategoryDomain {
    private final StoreCategoryRepository repository;
    private final StoreDomain storeDomain;

    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    @Override
    public StoreCategory detail(Long id) {
        return repository.detail(id);
    }

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public List<StoreCategoryRes> queryList(StoreCategoryQuery query) {
        List<StoreCategoryRes> voList = repository.queryList(query);
        return voList;
    }

    /**
     * 新增数据
     *
     * @param saveCommand 新增实体
     */
    @Override
    public Long add(StoreCategorySaveCommand saveCommand) {
        StoreCategory storeCategory = BeanUtil.copyProperties(saveCommand, StoreCategory.class);
        repository.insert(storeCategory);
        return storeCategory.getId();
    }

    /**
     * 修改数据
     *
     * @param saveCommand 编辑实体
     */
    @Override
    public void edit(StoreCategorySaveCommand saveCommand) {
        StoreCategory storeCategory = BeanUtil.copyProperties(saveCommand, StoreCategory.class);
        StoreCategoryQuery query = new StoreCategoryQuery();
        query.setId(saveCommand.getId());
        repository.edit(storeCategory, query);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    @Override
    public void del(Long id) {
        // 判断门店是否关联了该分类
        StoreQuery storeQueryReq = new StoreQuery();
        storeQueryReq.setType(id);
        storeQueryReq.resetQuerySingle();
        Page<StoreSearchRes> page = storeDomain.storeSearchPage(storeQueryReq);
        boolean hasStore = page.getTotal() > 0;
        if (hasStore) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "门店分类下存在门店，不允许删除");
        }

        repository.del(id);
    }

    /**
     * 查询分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    @Override
    public IPage<StoreCategoryRes> queryPage(StoreCategoryQuery query) {
        IPage<StoreCategoryRes> page = repository.queryPage(query);
        return page;
    }
}

