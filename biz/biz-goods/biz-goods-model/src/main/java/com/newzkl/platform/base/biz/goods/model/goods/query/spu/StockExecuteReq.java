package com.newzkl.platform.base.biz.goods.model.goods.query.spu;

import lombok.Data;

@Data
public class StockExecuteReq {
    /**
     * SKU_ID
     */
    private Long skuId;
    /**
     * 数量, 小于0 减少, 大于0新增
     */
    private Integer count;
}
