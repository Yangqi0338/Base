package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

/**
 * 订单状态记录表 DO
 *
 * @author sijiwang
 * @since 2026-01-30
 */
@Data
@TableName
public class OrderStateRecordDO extends BaseDO {

    /**
     * 交易单号
     */
    @Index
    @OldColumnName("order_id")
    private String orderNo;

    /**
     * SPU订单ID
     */
    @Index
    private Long spuOrderId;

    /**
     * SKU订单ID
     */
    @Index
    private Long skuOrderId;

    /**
     * 变更前订单状态
     */
    private OrderEnum.State beforeOrderState;

    /**
     * 变更后订单状态
     */
    private OrderEnum.State afterOrderState;

    /**
     * 下单人ID
     * @ext 匿名订单可为 null
     */
    @Index
    private Long ordererId;

    /**
     * 操作人角色
     */
    @OldColumnName("operatorRoleId")
    private AccountEnum.Identity operatorRole;

    /**
     * 拓展字段
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String ext;
}
