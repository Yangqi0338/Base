package com.newzkl.platform.base.biz.order.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * SKU 下单快照信息
 * <p>
 * SpuOrder 层裁撤后, 商品(SPU)/规格(SKU)冗余信息内嵌进 SkuOrder, 以 JSON 列持久化
 */
@Data
public class OrderSkuInfo implements Serializable {

    /**
     * SPU id
     */
    private Long spuId;

    /**
     * SPU 名称
     */
    private String spuName;

    /**
     * SPU 主图
     */
    private String spuImg;

    /**
     * SKU id
     */
    private Long skuId;

    /**
     * SKU 名称
     */
    private String skuName;

    /**
     * SKU 重量
     */
    private Double skuWeight;

    /**
     * SKU 体积
     */
    private Double skuVolume;
}
