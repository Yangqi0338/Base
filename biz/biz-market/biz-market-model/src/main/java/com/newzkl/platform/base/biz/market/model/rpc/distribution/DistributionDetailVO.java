package com.newzkl.platform.base.biz.market.model.rpc.distribution;

import com.newzkl.platform.base.biz.market.model.enums.SpuEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 铺货详情VO（包含铺货、SPU、SKU信息）
 *
 * @author
 */
@Data
public class DistributionDetailVO implements Serializable {
    // ========== 铺货表(store_distribution)字段 ==========
    private Long distributionId;       // 铺货ID（避免与spuId/skuId冲突）
    private Long goodsId;              // 商品ID（关联SPU）
    private Long skuId;                // SKU ID（关联SKU）
    private Long marketId;             // 来源
    private Integer dataType;          // 数据类型 0：商品  1：sku
    private Long sellPrice;         // 销售价 渠道商铺货价
    private Integer sellNum;           // 销量
    private Long storeId;              // 门店ID
    private Integer goodsState;        // 商品状态
    private Long channelId;            // 渠道商ID
    private LocalDateTime createTime;  // 创建时间
    private Integer unitPrice;         // 零售价
    private Long supplierPrice;     // 平台供货价 渠道商进价
    private String goodsInfo;          // 商品信息
    private Integer needUpdate;        // 是否需要更新
    private LocalDateTime upTime;      // 修改时间
    private LocalDateTime recommendationTime; // 推荐时间

    // ========== SPU表(spu)字段 ==========
    private Long spuId;                // SPU ID
    private Long outSpuId;
    private String spuName;            // SPU名称
    private String spuImg;             // SPU图片
    private String spuTitle;           // SPU标题
    private Integer spuState;          // SPU状态
    private Integer virtualSaleNum;    // 虚拟销量
    private Long categoryId;           // 分类ID
    private String categoryName;       // 分类名称
    private Long freightTemplateId;     // 运费模板ID
    private Long supplierId;           // 供货商ID
    private SpuEnum.ChannelType channelType;    // spu.channel_type
    // ========== SKU表(sku)字段 ==========
    private Long skuIdDetail;          // SKU ID（避免与铺货表skuId命名冲突）
    private Long outSkuId;
    private String skuName;            // SKU名称
    private String skuImg;             // SKU图片
    private Double weight;             // 重量(千克)
    private Double volume;             // 体积(m³)
    private Integer skuInventory;      // SKU库存
    private String saleAttribute;      // 销售属性(JSON)
    private Integer skuSalePrice;      // SKU销售价
    private Integer skuUnitPrice;      // SKU零售价
    private Long skuSupplyPrice;      //sku供货价(平台进价)
    private Integer buyNum;
}