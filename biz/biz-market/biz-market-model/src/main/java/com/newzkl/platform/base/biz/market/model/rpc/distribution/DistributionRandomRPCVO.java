package com.newzkl.platform.base.biz.market.model.rpc.distribution;

import lombok.Data;

import java.io.Serializable;

/**
 * 铺货VO
 */
@Data
public class DistributionRandomRPCVO implements Serializable {

    /**
     * 铺货id
     */
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 销售价
     */
    private Integer sellPrice;

    /**
     * 销量
     */
    private Integer sellNum;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品图片
     */
    private String img;
}
