package com.newzkl.platform.base.biz.market.model.vo.market;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 铺货商品列表返回对象
 * @date 2024/4/2 16:33
 */
@Data
public class DistributionGoodsListOPVO {

    /**
     * id
     */
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsPicture;

    /**
     * 售价
     */
    private String sellPrice;

    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;


    /**
     * 销量
     */
    private Integer sellNum;

    /**
     * 商品状态 0:下架  1：上架  -1：平台下架
     */
    private Integer goodsState;

    /**
     * 上架时间
     */
    private LocalDateTime upTime;

    /**
     * SKU ID集合（逗号分隔）
     */
    private String skuIds;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店Id
     */
    private String storeId;

    /**
     * 推荐时间
     */
    private LocalDateTime recommendationTime;

}
