package com.newzkl.platform.base.biz.order.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 交易单聚合
 * @date 2023/11/289:35
 */
@Data
public class OrderAgg implements Serializable {

    /**
     * 是否是初始化
     */
    private boolean isInit = true;
    /**
     * 交易单
     */
    private OrderDTO order;
    /**
     * spu订单
     */
    private List<SpuOrderDTO> spuOrderList;
    /**
     * sku订单
     */
    private List<SkuOrderDTO> skuOrderList;
}
