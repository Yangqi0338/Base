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
    @NotNull
    private Long storeId;

    /**
     * 账号id(account.id)
     */
    private Long accountId;

    /**
     * 订单号
     */
    @NotNull
    private String orderNo;

    /**
     * 收货信息值对象
     */
    @NotNull
    private ShipVO shipVO;
}
