package com.newzkl.platform.base.biz.market.model.query.distribution;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 铺货列表
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreDistributionQuery extends BizPageQuery {

    /**
     * 状态
     *
     * @ext 0：下架 1：上架
     */
    private Integer state;

    /**
     * 状态 (排除)
     *
     * @ext 0：下架 1：上架
     */
    private Integer stateNot;


    /**
     * 数据类型
     *
     * @ext 0：商品 1：sku
     */
    private Integer dataType;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 渠道商id
     */
    private Long channelId;

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