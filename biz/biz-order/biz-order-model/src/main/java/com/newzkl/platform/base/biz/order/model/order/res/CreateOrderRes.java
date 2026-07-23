package com.newzkl.platform.base.biz.order.model.order.res;

import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建订单响应参数
 * @author sijiwang
 */
@Data
public class CreateOrderRes implements Serializable {

    /**
     * 交易单
     */
    private Order order;

    /**
     * 订单商品明细列表
     */
    private List<OrderItem> orderItems;

    /**
     * 支付倒计时
     */
    private Long remainTime;

    @Data
    public static class OrderItem {

        /**
         * 订单商品
         */
       private SpuOrder spuOrder;

       /**
         * 订单商品明细列表
         */
       private List<SkuOrder> skuOrders;
    }
}
