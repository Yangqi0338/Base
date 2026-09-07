package com.newzkl.platform.base.biz.goods.rpc.model.relation;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 渠道商商品关系信息
 * @date 2024/1/216:06
 */
@Data
public class SkuSaleInfo implements Serializable {

    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * ID
     */
    private Long skuId;
    /**
     * 图片
     */
    private String img;
    /**
     * 补偿字段
     */
    private String saleAttributeJson;
    /**
     * 重量(千克)
     */
    private Double weight;
    /**
     * 体积(m3)
     */
    private Double volume;
    /**
     * spuId (查询)
     */
    private Long spuId;
    /**
     * 供货价
     */
    private Integer supplyPrice;
    /**
     * 销售价
     */
    private Integer salePrice;
    /**
     * 铺货价
     */
    private Integer storePrice;
    /**
     * 运费模板id
     */
    private Long freightTemplateId;
    /**
     * 状态 0:仓库中 2:上架中 3:待上架 (查询)
     */
    private Integer spuState;
    /**
     * spu图片
     */
    private String spuImg;
    /**
     * spu名称
     */
    private String spuName;
    /**
     * spu销售类型 0 实物
     */
    private Integer spuSaleType;
    /**
     * 渠道类型 0 供货商品 1 自营商品
     */
    private Integer spuChannelType;
}
