package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

/**
 * 订单状态记录分页查询请求对象
 *
 * @author sijiwang
 * @since 2026-01-30
 */
@Data
public class OrderStateRecordQuery extends BizPageQuery {

    /**
     * 交易单号
     */
    private String orderNo;

    /**
     * SPU订单ID
     */
    private Long spuOrderId;

    /**
     * 变更后订单状态
     * @ext 前端传数字 code, Jackson 经 OrderEnum.State 的 @JsonValue 反序列化为枚举
     */
    private OrderEnum.State afterOrderState;

    /**
     * 操作人角色
     * @ext 前端传数字 code, Jackson 经 RoleEnum.CompanyRole 的 @JsonValue 反序列化为枚举
     */
    private AccountEnum.Identity operatorRoleId;
}
