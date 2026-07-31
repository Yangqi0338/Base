package com.newzkl.platform.base.biz.order.model.req;


import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 提交预支付订单请求对象
 * @Author: niu
 * @Date: 2023/4/19 14:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderCreateCommand {
    /**
     * 订单类型 (0:渠道商订单 1:C端订单) 查询
     */
    @NotNull(message = "orderType?")
    private OrderEnum.OrderType orderType;
    /**
     * 收货信息值对象
     */
    @NotNull(message = "shipVO?")
    private ShipVO shipVO;
    /**
     * 渠道商ID 查询
     */
    private Long channelId;
    /**
     * 收益三方账号
     */
    private String benefitTripartiteId;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 外部订单号
     */
    private String outOrderNo;
    /**
     * 商品信息
     */
    @NotEmpty(message = "orderGoodsList?")
    private List<OrderItemCommand> orderGoodsList;
    private Long operatorId;
}
