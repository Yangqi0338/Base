package com.newzkl.platform.base.biz.order.model.dto;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2019:39
 */
@Data
public class OrderStateCheckDTO {
    private Long id;
    private String orderNo;
    private OrderEnum.State currentState;
    private OrderEnum.State toState;
    private String outOrderNo;
    private Long channelId;
    private OrderEnum.OrderType orderType;
}
