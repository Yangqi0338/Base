package com.newzkl.platform.base.biz.order.facade.model.api.order;


import com.newzkl.platform.base.biz.order.facade.model.common.SkuSaleAttributeApiVO;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
* SKU信息
* @author fang
*/
@Data
public class ApiSkuOrderVO  implements Serializable {
    /**
     * SKU_ID
     */
    @NotNull
    private Long skuId;
    /**
     * 订单ID
     */
    @NotNull
    private Long orderId;
    /**
     * 销售价
     */
    @NotNull
    private Integer salePrice;
    /**
     * spuId
     */
    @NotNull
    private Long spuId;
    /**
     * 购买数量
     */
    @NotNull
    private Integer count;
    /**
     * sku图片
     */
    @NotNull
    private String skuImg;
    /**
     * sku销售属性
     */
    @NotNull
    private List<SkuSaleAttributeApiVO> skuSaleAttribute;
    /**
     * sku名称
     */
    private String skuName;
    /**
     * sku重量(千克)
     */
    private Double skuWeight;
    /**
     * sku体积(m3)
     */
    private Double skuVolume;
}
