package com.newzkl.platform.base.biz.order.model.event;

import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单发货业务事件
 *
 * <p>由 biz-order 在发货单创建成功后无条件发布, 订阅方自行按 orderType 过滤。
 * 承载收件人与订单类型是为了让订阅方零回查即可路由 —— Base 不感知任何订阅方。
 * 字段名对齐对外契约体 {@code ApiDeliverEvent} 以便订阅方直拷</p>
 *
 * @author KC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveryEvent implements Serializable {

    /**
     * 外部订单号
     */
    private String outOrderNo;

    /**
     * 下单主体账户ID 乐态订单即渠道商
     */
    private Long channelId;

    /**
     * 订单类型
     */
    private OrderEnum.OrderType orderType;

    /**
     * 本次发货的SKU与数量
     */
    private List<SkuCountDTO> skuDeliverList;

    /**
     * 快递公司名称
     */
    private String expressName;

    /**
     * 快递单号
     */
    private String expressNo;

}
