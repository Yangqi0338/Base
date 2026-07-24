package com.newzkl.platform.base.biz.store.model.store.req;

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
    private static final long serialVersionUID = -27178724794239298L;

    /**
     * 门店名称
     */
    @Size(max = 50, message = "超出50字符上限")
    private String name;

}

