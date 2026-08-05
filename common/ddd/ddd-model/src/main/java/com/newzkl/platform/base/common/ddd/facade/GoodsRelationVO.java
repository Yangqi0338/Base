package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;

@Data
public class GoodsRelationVO {
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 售价区间
     */
    private String sellPrice;
}
