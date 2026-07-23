package com.newzkl.platform.base.biz.market.model.query.distribution;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 铺货列表随机分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DistributionRandomPageQuery extends PageQuery {

    /**
     * 类目ID
     */
    private Long categoryId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 铺货价左
     */
    private Integer sellPriceL;

    /**
     * 铺货价右
     */
    private Integer sellPriceR;

    /**
     * 是否推荐
     */
    private Boolean isRecommended;
}
