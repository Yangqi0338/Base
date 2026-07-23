package com.newzkl.platform.base.biz.market.model.vo.market;

import lombok.Data;

/**
 * 铺货VO
 */
@Data
public class DistributionRandomVO {
    private Long id;

    /**
     * skuId
     */
    private Long skuId;

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

    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;

}
