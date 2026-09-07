package com.newzkl.platform.base.biz.goods.rpc.model.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 铺货详情VO（三表联查结果）
 * @author yourname
 */
@Data
public class StoreGoodsDetailRpcVO implements Serializable {
    /** 铺货表ID */
    private Long id;
    /** spu.id */
    private Long goodsId;
    /** sku.id */
    private Long skuId;
    /** 市场id 0：自营 >0：市场id */
    private Long marketId;
    /** 数据类型 0：商品  1：sku */
    private Integer dataType;
    /** 销售价 */
    private Integer sellPrice;
    /** 销量 */
    private Integer sellNum;
    /** 门店id */
    private Long storeId;
    /** 商品状态 1:下架  0：上架  -1：平台下架 */
    private Integer goodsState;
    /** 渠道商id */
    private Long channelId;
    /** 零售价（铺货表） */
    private Integer unitPrice;
    /** 供货价（铺货表） */
    private Integer supplierPrice;
    /** 来源 */
    private String source;
    /** 拓展信息 */
    private String expand;

    /** sku.img */
    private String skuImg;
    /** sku.bar_code */
    private String barCode;
    /** sku.name */
    private String skuName;
    /** sku.weight */
    private BigDecimal weight;
    /** sku.volume */
    private BigDecimal volume;
    /** sku.supply_price */
    private Integer skuSupplyPrice;
    /** sku.out_sku_id */
    private String outSkuId;
    /** sku.sale_price_rate */
    private Float salePriceRate;
    /** sku.buy_start_qty */
    private Integer buyStartQty;
    /** sku.unit_price */
    private Integer skuUnitPrice;
    /** 销售属性 */
    private String saleAttribute;

    /** spu.account_id 供应商id */
    private Long supplierId;
    /** spu.freight_template_id */
    private Long freightTemplateId;
    /** spu.state */
    private Integer spuState;
    /** spu.img */
    private String spuImg;
    /** spu.name */
    private String spuName;
    /** spu.out_spu_id */
    private String outSpuId;
    /** spu.channel_type */
    private Integer channelType;
    /** spu.supply_price */
    private Integer spuSupplyPrice;

    /** 购买数量 */
    private Integer bugNum;
}