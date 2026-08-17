package com.newzkl.platform.base.biz.order.model.dto;

import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单状态记录表
 * @author sijiwang
 */
@Data
public class OrderStateRecordEntity {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单主键ID（关联订单表）
     */
    private Long orderId;

    /**
     * SPU订单ID（关联SPU订单表）
     */
    private Long spuOrderId;

    /**
     * SKU订单ID（关联SKU订单表）
     */
    private Long skuOrderId;

    /**
     * 变更前订单状态
     */
    private OrderEnum.State beforeOrderState;

    /**
     * 变更前订单状态描述（如：新订单、待发货、已完成）
     */
    private String beforeStateDesc;

    /**
     * 变更后订单状态
     */
    private OrderEnum.State afterOrderState;

    /**
     * 变更后订单状态描述（如：新订单、待发货、已完成）
     */
    private String afterStateDesc;

    /**
     * 下单人ID（关联用户表，匿名订单可为null）
     */
    private Long ordererId;

    /**
     * 操作人ID（关联用户表；系统自动操作时为null）
     */
    private Long operatorId;

    /**
     * 操作人角色
     */
    private RoleEnum.CompanyRole operatorRoleId;

    /**
     * 角色描述（如：C端客户、系统、平台管理员）
     */
    private String roleDesc;

    /**
     * 操作时间（订单状态变更的实际时间）
     */
    private LocalDateTime operateTime;

    /**
     * 拓展字段（JSON格式存储额外信息：如操作备注、客户端类型、操作IP等）
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