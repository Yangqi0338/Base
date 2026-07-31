package com.newzkl.platform.base.biz.order.model.res;

import lombok.Data;

/**
 * @author muc_fang
 * @Description: 订单商品发货信息
 * @date 2023/5/914:58
 */
@Data
public class AlreadyDeliverRes {
    /**
     * SkuID
     */
    private Long skuId;
    /**
     * 购买数量
     */
    private Integer orderCount;
    /**
     * 发货数量
     */
    private Integer deliverCount;
}
