package com.newzkl.platform.base.biz.order.model.req;


import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 订单变更收货地址
 */
@Data
public class OrderShipCommand {

    /**
     * 门店ID
     */
    @NotNull(message = "storeId不能为空")
    private Long storeId;

    /**
     * 账号id(account.id)
     */
    private Long accountId;

    /**
     * 订单ID
     */
    @NotNull(message = "orderId?")
    private Long orderId;

    /**
     * 收货信息值对象
     */
    @NotNull(message = "shipVO?")
    private ShipVO shipVO;
}
