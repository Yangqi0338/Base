package com.newzkl.platform.base.biz.store.model.store.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import jakarta.validation.constraints.Size;


/**
 * 门店分类 #store(StoreCategory)查询类
 *
 * @author kc
 * @since 2026-01-27 11:21:02
 */
@Data
public class StoreCategoryQuery extends BizPageQuery {
    /**
     * 门店名称
     */
    @Size(max = 50)
    private String name;
}

