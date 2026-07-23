package com.newzkl.platform.base.biz.market.model.dto.distribution;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 * 铺货列表
 */
@Data
public class StoreDistributionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * skuId
     */
    private Long skuId;
    /**
     * 来源
     * @see com.newzkl.platform.base.biz.market.model.enums.DistributionEnum.Source
     */
    private Long marketId;
    /**
     * 数据类型 0：商品  1：sku
     */
    private Integer dataType;
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
     * 商品状态 0:下架  1：上架  -1：回收站
     */
    private Integer goodsState;
    /**
     * 渠道商id
     */
    private Long channelId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 建议零售价
     */
    private Integer unitPrice;
    /**
     * 供货价
     */
    private Integer supplierPrice;
    /**
     * 商品信息
     */
    private String goodsInfo;
    /**
     * 是否需要更新：0不需要，1需要
     */
    private Integer needUpdate;
    /**
     * 上架时间
     */
    private LocalDateTime upTime;
}