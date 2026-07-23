package com.newzkl.platform.base.biz.order.model.order.vo;

import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import lombok.Data;

import java.util.List;

/**
 * @author sijiwang
 */
@Data
public class SpuOrderAggVO {

    /**
     * SPU订单信息
     */
    private SpuOrderVO spuOrderVO;

    /**
     * SKU订单列表
     */
    private List<SkuOrder> skuOrderList;
}
