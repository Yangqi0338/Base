package com.newzkl.platform.base.biz.order.facade.model.api.order;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
* 订单信息
* @author fang
*/
@Data
public class ApiOrderVO implements Serializable {
    /**
     * 订单号
     */
    @NotNull
    private Long id;
    /**
     * 外部订单号
     */
    private String outOrderNo;
    /**
     * 商品金额
     */
    @NotNull
    private Integer goodsAmount;
    /**
     * 运费金额
     */
    @NotNull
    private Integer freightAmount;
    /**
     * 优惠金额
     */
    @NotNull
    private Integer discountAmount;
    /**
     * 订单金额
     */
    @NotNull
    private Integer totalAmount;
    @NotNull
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 订单状态 (0, "新订单"),(2,"待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭")
     */
    @NotNull
    private OrderEnum.State orderState;
    /**
     * 收货信息
     */
    @NotNull
    private ApiShipVO shipVO;
}
