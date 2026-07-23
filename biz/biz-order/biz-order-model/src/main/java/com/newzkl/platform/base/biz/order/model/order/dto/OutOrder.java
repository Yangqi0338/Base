package com.newzkl.platform.base.biz.order.model.order.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 外部订单表 DTO
 *
 * @author sijiwang
 */
@Data
public class OutOrder {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 外部订单号
     */
    private String orderSn;

    /**
     * 内部订单ID
     */
    private Long orderId;

    /**
     * SKU_ID
     */
    private String skuIds;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}