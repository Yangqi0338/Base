package com.newzkl.platform.base.biz.order.model.order.req;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单状态记录分页查询请求对象
 *
 * @author sijiwang
 * @since 2026-01-30
 */
@Data
public class OrderStateRecordPageReq {
    /**
     * 当前页码
     */
    private Long current = 1L;

    /**
     * 页大小
     */
    private Long size = 10L;

    /**
     * 订单主键ID（可选）
     */
    private String orderNo;

    /**
     * SPU订单ID（可选）
     */
    private String spuOrderNo;

    /**
     * 变更后订单状态（可选）
     */
    private Integer afterOrderState;

    /**
     * 操作人角色ID（可选）
     */
    private Long operatorRoleId;

}