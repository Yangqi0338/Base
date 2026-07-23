package com.newzkl.platform.base.biz.market.domain.support;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;

import java.util.List;

/**
 * 分类通用 Repository 接口
 *
 * @param <V> VO 类型
 * @param <Q> 查询类型（extends CategoryQuery）
 * @author fang
 */
public interface CategoryRepository<V, Q extends CategoryQuery> {

    void categorySave(CategoryReq categoryReq);

    void categoryDelete(List<Long> idList);

    void categoryEdit(CategoryReq categoryReq);

    V category(Long id);

    Long categoryCount(Q categoryQuery);

    Page<V> categoryPage(Q categoryQuery);

    List<Long> idList(Q categoryQuery);

    List<V> categoryList(Q categoryQuery);

    boolean existsByName(String name, Long excludeId);

}
