package com.newzkl.platform.base.biz.goods.rpc.model.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 铺货详情VO（三表联查结果）
 * @author yourname
 */
@Data
public class StoreDistributionDetailRpcVO implements Serializable {
    // ------------------- 铺货表字段 -------------------
    private Long id;                // 铺货表ID
    private Long goodsId;           // spu.id
    private Long skuId;             // sku.id
    private Long marketId;          // 市场id 0：自营 >0：市场id
    private Integer dataType;       // 数据类型 0：商品  1：sku
    private Integer sellPrice;      // 销售价
    private Integer sellNum;        // 销量
    private Long storeId;           // 门店id
    private Integer goodsState;     // 商品状态 1:下架  0：上架  -1：平台下架
    private Long channelId;         // 渠道商id
    private Integer unitPrice;      // 零售价（铺货表）
    private Integer supplierPrice;  // 供货价（铺货表）
    private String source;          // 来源
    private String expand;          // 拓展信息
    // ------------------- SKU表字段（加别名避免冲突） -------------------
    private String skuImg;          // sku.img
    private String barCode;         // sku.bar_code
    private String skuName;         // sku.name
    private BigDecimal weight;      // sku.weight
    private BigDecimal volume;      // sku.volume
    private Integer skuSupplyPrice; // sku.supply_price
    private Integer inventory;      // sku.inventory
    private String outSkuId;        // sku.out_sku_id
    private Float salePriceRate;    // sku.sale_price_rate
    private Integer buyStartQty;    // sku.buy_start_qty
    private Integer skuUnitPrice;   // sku.unit_price
    private String saleAttribute;
    // ------------------- SPU表字段（加别名避免冲突） -------------------
    private Long supplierId;         // spu.account_id 供应商id
    private Long freightTemplateId; // spu.freight_template_id
    private Integer spuState;       // spu.state
    private String spuImg;          // spu.img
    private String spuName;         // spu.name
    private String outSpuId;        // spu.out_spu_id
    private Integer channelType;    // spu.channel_type
    private Integer spuSupplyPrice; // spu.supply_price

    // ------------------- 业务计算字段（非数据库存储） -------------------
    private Integer bugNum;
}