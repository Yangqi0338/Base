package com.newzkl.platform.base.biz.goods.model.goods.req.spu;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class OutSpuEditCommand {
    /**
     * spu_id
     */
    @NotNull
    private Long spuId;
    /**
     * 所属平台分类 (查询)
     */
    @NotNull(message = "categoryId?")
    private Long categoryId;
    /**
     * 所属平台分类名称完整
     */
    private String categoryName;
    /**
     * 品牌id (查询)
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * sku销售价: key:sku_id, value:销售价
     */
    @NotEmpty
    private Map<Long, Integer> skuSalePrice;

    /**
     * 建议零售价: key:sku_id, value:零售价
     */
    private Map<Long, Integer> skuUnitPrice;
}