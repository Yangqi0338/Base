package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreCategory;
import com.newzkl.platform.base.biz.store.model.store.req.StoreCategoryQuery;
import com.newzkl.platform.base.biz.store.model.store.vo.StoreCategoryVO;

import java.util.List;

/**
 * 门店分类 (store_category)存储接口
 *
 * @author kc
 * @since 2026-01-27 11:21:02
 */
public interface IStoreCategoryRepository {
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
    List<StoreCategoryVO> queryList(StoreCategoryQuery query);

    /**
     * 查询分页
     *
     * @param query 查询条件
     * @return 分页
     */
    IPage<StoreCategoryVO> queryPage(StoreCategoryQuery query);

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

