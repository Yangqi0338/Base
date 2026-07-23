package com.newzkl.platform.base.biz.goods.model.goods.query.spu;

import com.newzkl.platform.base.biz.goods.model.biz.req.query.CategoryQuery;
import lombok.Data;

/**
 * 分类
 *
 * @author fang
 */
@Data
public class SpuCategoryQuery extends CategoryQuery {
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 行业ID
     */
    private Long industryId;
}
