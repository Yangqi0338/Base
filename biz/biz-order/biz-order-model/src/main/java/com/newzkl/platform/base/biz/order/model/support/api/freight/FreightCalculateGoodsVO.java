package com.newzkl.platform.base.biz.order.model.support.api.freight;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author niu
 * @description: 运费计算商品信息VO
 * @date 2023/4/28 11:14
 */
@Data
public class FreightCalculateGoodsVO implements Serializable {

    /**
     * spuId
     */
    private Long spuId;
    /**
     * skuId
     */
    private Long skuId;

    /**
     * sku数量
     */
    private Integer skuNum;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 体积
     */
    private BigDecimal volume;

    /**
     * 模板id
     */
    private Long templateId;
}
