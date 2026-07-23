package com.newzkl.platform.base.biz.market.model.vo.market;

import lombok.Data;

/**
 * @author niu
 * @description: 铺货商品信息
 * @date 2024/4/2 14:51
 */
@Data
public class DistributionGoodsInfoVO {

    /**
     * skuId
     */
    private Long skuId = 0L;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 销售价格
     */
    private Integer sellPrice;

    /**
     * 市场id
     */
    private Long marketId;

    private Long categoryId;

    /**
     * 零售价(渠道商的供货价)
     */
    private Integer unitPrice;

    /**
     * 来源
     */
    private String source;

}
