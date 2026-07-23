package com.newzkl.platform.base.biz.order.model.order.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单发货分页查询请求
 * @author sijiwang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDeliveryPageReq extends Page<OrderDelivery> {

    /**
     * 发货单号
     */
    private String deliveryNo;

    /**
     * 主订单号
     */
    private String orderNo;

    /**
     * SPU订单号
     */
    private String spuOrderNo;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 发货状态
     */
    private Integer deliveryStatus;
}