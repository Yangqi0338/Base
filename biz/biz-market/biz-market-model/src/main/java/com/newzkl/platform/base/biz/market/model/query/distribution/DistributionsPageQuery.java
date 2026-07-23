package com.newzkl.platform.base.biz.market.model.query.distribution;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 查询铺货列表分页查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DistributionsPageQuery extends PageQuery {

    /**
     * 状态 0：下架  1：上架
     */
    private Integer state;

    /**
     * 状态 0：下架  1：上架
     */
    private Integer stateNot;

    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 零售价左
     */
    private Integer sellPriceL;

    /**
     * 零售价右
     */
    private Integer sellPriceR;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 上架时间左
     */
    private String upTimeL;

    /**
     * 上架时间右
     */
    private String upTimeR;

    /**
     * 关联skuId
     */
    private Long skuId;

    /**
     * 商品id集合
     */
    private List<Long> goodsIds;

    /**
     * 门店id集合
     */
    private List<Long> storeIds;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 是否推荐
     */
    private Boolean isRecommended;
}
