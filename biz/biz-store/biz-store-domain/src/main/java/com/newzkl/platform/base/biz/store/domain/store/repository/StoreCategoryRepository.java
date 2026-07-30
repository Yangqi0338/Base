package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreCategory;
import com.newzkl.platform.base.biz.store.model.store.req.StoreCategoryQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreCategoryRes;

import java.util.List;

/**
 * 门店分类 (store_category)存储接口
 *
 * @author kc
 * @since 2026-01-27 11:21:02
 */
public interface StoreCategoryRepository {
    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    StoreCategory detail(Long id);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<StoreCategoryRes> queryList(StoreCategoryQuery query);

    /**
     * 查询分页
     *
     * @param query 查询条件
     * @return 分页
     */
    Page<StoreCategoryRes> queryPage(StoreCategoryQuery query);

    /**
     * 新增数据
     *
     * @param storeCategory 新增实体
     */
    void insert(StoreCategory storeCategory);

    /**
     * 修改数据
     *
     * @param storeCategory 编辑实体
     * @param query         编辑查询
     */
    void edit(StoreCategory storeCategory, StoreCategoryQuery query);

    /**
     * 删除
     *
     * @param id 主键
     */
    void del(Long id);

}

