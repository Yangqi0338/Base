package com.newzkl.platform.base.biz.market.domain.category.service;

import com.newzkl.platform.base.biz.market.model.biz.req.CategoryEditReq;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.market.model.biz.req.CategorySyncReq;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;

import java.util.List;

/**
 * 商户分类领域服务。
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.category.service.ICategoryDomain},
 * 按新规范去 {@code I} 前缀。</p>
 *
 * @author KC
 */
public interface CategoryDomain {

    /**
     * 新建单条自营分类。
     *
     * @param req 分类请求
     * @return 新分类ID
     */
    Long categorySave(CategoryReq req);

    /**
     * 编辑分类。
     *
     * @param req 编辑请求 (id + 分类内容)
     */
    void categoryEdit(CategoryEditReq req);

    /**
     * 删除分类。
     *
     * @param idList 分类ID列表
     */
    void categoryDelete(List<Long> idList);

    /**
     * 查询分类树。
     *
     * @param query 查询条件
     * @return 树形分类列表, 恒非 null
     */
    List<CategoryVO> categoryTree(CategoryQuery query);

    /**
     * 同步平台分类到当前商户。
     *
     * <p>层级模型由旧 {@code code}/{@code pcode} (平台分类ID) 改为本表自身
     * {@code pid} 制, 故需两趟 remap: 第一趟为每个平台节点分配新雪花 id 并建立
     * {@code 平台id -> 新id} 映射, 第二趟按映射改写 {@code pid}; 平台源 id
     * 落 {@code sourceId} 列供幂等与再次同步 diff。</p>
     *
     * @param req 同步请求 (id 为平台根分类ID)
     */
    void syncCategory(CategorySyncReq req);
}
