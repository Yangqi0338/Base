package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单状态记录视图对象
 *
 * @author sijiwang
 * @since 2026-01-30
 */
@Data
public class OrderStateRecordVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单主键ID
     */
    private Long orderId;

    /**
     * SPU订单ID
     */
    private Long spuOrderId;

    /**
     * SKU订单ID
     */
    private Long skuOrderId;

    /**
     * 变更前订单状态
     */
    private OrderEnum.State beforeOrderState;

    /**
     * 变更前订单状态描述
     */
    private String beforeStateDesc;

    /**
     * 变更后订单状态
     */
    private OrderEnum.State afterOrderState;

    /**
     * 变更后订单状态描述
     */
    private String afterStateDesc;

    /**
     * 下单人ID
     * @ext 匿名订单可为 null
     */
    private Long ordererId;

    /**
     * 操作人ID
     * @ext 系统自动操作时为 null
     */
    private Long operatorId;

    /**
     * 操作人角色
     */
    private RoleEnum.CompanyRole operatorRoleId;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 拓展字段
     * @ext JSON 结构, 存额外信息
     */
    private String ext;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;
}
