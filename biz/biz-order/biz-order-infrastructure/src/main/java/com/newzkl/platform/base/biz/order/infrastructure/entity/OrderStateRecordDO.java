package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 订单状态记录表 DO
 *
 * @author sijiwang
 * @since 2026-01-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class OrderStateRecordDO extends BaseDO {

    /**
     * 订单主键ID（关联订单表）
     */
    @Index
    private String orderNo;

    /**
     * SPU订单ID（关联SPU订单表）
     */
    @Index
    private String spuOrderNo;

    /**
     * SKU订单ID（关联SKU订单表）
     */
    private String skuOrderNo;


    /**
     * 变更前订单状态（对应OrderEnum.State的code：0=新订单，1=C端待付款，2=渠道商待付款，3=运营商待付款，4=派发中，6=待发货，8=待收货，10=已收货，12=已完成，14=售后中，99=已关闭）
     */
    private Integer beforeOrderState;

    /**
     * 变更前订单状态描述（如：新订单、待发货、已完成）
     */
    private String beforeStateDesc;

    /**
     * 变更后订单状态（对应OrderEnum.State的code：0=新订单，1=C端待付款，2=渠道商待付款，3=运营商待付款，4=派发中，6=待发货，8=待收货，10=已收货，12=已完成，14=售后中，99=已关闭）
     */
    private Integer afterOrderState;

    /**
     * 变更后订单状态描述（如：新订单、待发货、已完成）
     */
    private String afterStateDesc;

    /**
     * 下单人ID（关联用户表，匿名订单可为null）
     */
    @Index
    private Long ordererId;

    /**
     * 操作人ID（关联用户表；系统自动操作时为null）
     */
    private Long operatorId;

    /**
     * 操作人角色ID（对应角色枚举code：如1000=C端客户，1001=供应商，1002=渠道商，0=平台，-1=系统）
     */
    private Long operatorRoleId;

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

}