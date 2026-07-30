package com.newzkl.platform.base.biz.store.domain.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.command.StoreCategorySaveCommand;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreCategory;
import com.newzkl.platform.base.biz.store.model.store.req.StoreCategoryQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreCategoryRes;

import java.util.List;

/**
 * 门店分类 (store_category)存储接口
 *
 * @author kc
 * @since 2026-01-27 11:21:03
 */
public interface StoreCategoryDomain {
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
     * 新增数据
     *
     * @param saveCommand 新增实体
     */
    Long add(StoreCategorySaveCommand saveCommand);

    /**
     * 修改数据
     *
     * @param saveCommand 编辑实体
     */
    void edit(StoreCategorySaveCommand saveCommand);

    /**
     * 删除
     *
     * @param id 主键
     */
    void del(Long id);

    /**
     * 查询分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    Page<StoreCategoryRes> queryPage(StoreCategoryQuery query);

}

