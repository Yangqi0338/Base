package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.money.Money;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品spu信息
 *
 * @author wqm
 * @since 2023年04月27日 14:58:00
 */
@Data
public class ApiSkuVO implements Serializable {
    /**
     * SKU_ID
     */
    @NotNull
    private Long id;
    /**
     * 图片
     */
    private String img;
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 商品销售属性
     */
    @NotNull
    private List<ApiSkuSaleAttributeVO> saleAttribute;
    /**
     * 重量(千克)
     */
    private Double weight;
    /**
     * 名称
     */
    private String name;
    /**
     * 体积(m3)
     */
    private Double volume;
    /**
     * spuId
     */
    @NotNull
    private Long spuId;
    /**
     * 市场价
     */
    private Money marketPrice;
    /**
     * 采购价
     */
    @NotNull
    private Money salePrice;
    /**
     * 起购数量
     */
    @NotNull
    private Integer buyStartQty;

    /**
     * 供货价
     */
    private Money supplyPrice;

    /**
     * 冗余: 建议零售价(to c)
     */
    private Money unitPrice;

    /**
     * 外部SkuId
     */
    private String outSkuId;
}
