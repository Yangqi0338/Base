package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import lombok.Data;

/**
 * sku
 *
 * @author fang
 */
@Data
public class SkuExpandVO {
    /**
     * 渠道类型
     */
    private String channelType;
    /**
     * 商品编码
     */
    private String itemCode;
    /**
     * 单位
     */
    private String unit;
}