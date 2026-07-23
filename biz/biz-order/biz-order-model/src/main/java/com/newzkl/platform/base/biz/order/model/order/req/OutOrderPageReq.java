package com.newzkl.platform.base.biz.order.model.order.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OutOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 外部订单分页查询请求
 *
 * @author sijiwang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OutOrderPageReq extends Page<OutOrder> {

    /**
     * 外部订单号
     */
    private String orderSn;

    /**
     * 内部订单ID
     */
    private Long orderId;

}