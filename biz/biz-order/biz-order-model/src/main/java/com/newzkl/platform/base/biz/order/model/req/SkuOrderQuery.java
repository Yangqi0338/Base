package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* SKU订单
* @author fang
*/
@Data
public class SkuOrderQuery extends PageQuery {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * SKU_ID集合
     */
    private List<Long> skuIdList;
    private OrderEnum.State orderState;
    /**
     * 小于发货时间
     */
    private LocalDateTime lessDeliverTime;
    /**
     * 小于收货时间
     */
    private LocalDateTime lessReceiveTime;
    private Long orderId;
    /**
     * 订单ID集合
     */
    private List<Long> orderIdList;
    /**
     * SPU订单ID
     */
    private Long spuOrderId;
    /**
     * SPU订单ID集合
     */
    private List<Long> spuOrderIdList;
    /**
     * 售后中数量
     */
    private Integer refundingCount;
    /**
     * 结算发送状态
     */
    private Integer settleSendState;
    /**
     * 订单状态集合
     */
    private List<Integer> orderStateList;
}
