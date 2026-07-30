package com.newzkl.platform.base.biz.order.model.enums.order;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单域错误码
 *
 * <p>迁移: 原 {@code com.zkl.scm.model.exception.goods.OrderErrorCode}, 实现平台统一 {@code ErrorCode} 接口。</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    /** 存在非选品商品。 */
    NOT_SELECT(2001, "存在非选品商品:{}"),
    /** 存在超出配送区域的商品。 */
    PARAM_CREATE_GOODS_DELIVER(2002, "存在超出配送区域的商品:{}"),
    /** 余额不足。 */
    AMOUNT_LESS(2003, "余额不足"),
    /** 未找到订单。 */
    NOT_EXISTS(2004, "未找到订单"),
    /** 订单状态异常。 */
    STATE_ERROR(2010, "订单状态异常"),
    /** 售后失败。 */
    REFUND_FAIL(2011, "售后失败:{}"),
    ;

    /** 状态码。 */
    private final Integer code;
    /** 状态码对应说明文案。 */
    private final String message;
}
