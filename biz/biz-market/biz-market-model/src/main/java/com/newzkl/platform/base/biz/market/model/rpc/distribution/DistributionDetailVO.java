package com.newzkl.platform.base.biz.market.model.rpc.distribution;

import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
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
    /** 铺货ID（避免与spuId/skuId冲突） */
    private Long distributionId;
    /** 商品ID（关联SPU） */
    private Long goodsId;
    /** SKU ID（关联SKU） */
    private Long skuId;
    /** 来源 */
    private Long marketId;
    /** 数据类型 0：商品  1：sku */
    private Integer dataType;
    /** 销售价 渠道商铺货价 */
    private Long sellPrice;
    /** 销量 */
    private Integer sellNum;
    /** 门店ID */
    private Long storeId;
    /** 商品状态 */
    private Integer goodsState;
    /** 渠道商ID */
    private Long channelId;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 零售价 */
    private Integer unitPrice;
    /** 平台供货价 渠道商进价 */
    private Long supplierPrice;
    /** 商品信息 */
    private String goodsInfo;
    /** 是否需要更新 */
    private Integer needUpdate;
    /** 修改时间 */
    private LocalDateTime upTime;
    /** 推荐时间 */
    private LocalDateTime recommendationTime;

    /** SPU ID */
    private Long spuId;
    /** 外部SPU ID */
    private Long outSpuId;
    /** SPU名称 */
    private String spuName;
    /** SPU图片 */
    private String spuImg;
    /** SPU标题 */
    private String spuTitle;
    /** SPU状态 */
    private Integer spuState;
    /** 虚拟销量 */
    private Integer virtualSaleNum;
    /** 分类ID */
    private Long categoryId;
    /** 分类名称 */
    private String categoryName;
    /** 运费模板ID */
    private Long freightTemplateId;
    /** 供货商ID */
    private Long supplierId;
    /** spu.channel_type */
    private SpuEnum.ChannelType channelType;

    /** SKU ID（避免与铺货表skuId命名冲突） */
    private Long skuIdDetail;
    /** 外部SKU ID */
    private Long outSkuId;
    /** SKU名称 */
    private String skuName;
    /** SKU图片 */
    private String skuImg;
    /** 重量(千克) */
    private Double weight;
    /** 体积(m³) */
    private Double volume;
    /** 销售属性(JSON) */
    private String saleAttribute;
    /** SKU销售价 */
    private Integer skuSalePrice;
    /** SKU零售价 */
    private Integer skuUnitPrice;
    /** sku供货价(平台进价) */
    private Long skuSupplyPrice;
    /** 购买数量 */
    private Integer buyNum;
}