package com.newzkl.platform.base.biz.finance.model.enums.finance;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 财务域错误码
 *
 * <p>迁移合并: 原 scm-common 中 {@code FinanceErrorCode}、{@code ConfigErrorCode}、
 * {@code OrderErrorCode} 分散的三方账户/提现/订单错误码, 收敛为财务域自有错误码。</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum FinanceErrorCode implements ErrorCode {

    /** 订单不存在 (原 OrderErrorCode.NOT_EXISTS 2004)。 */
    NOT_EXISTS(2004, "未找到订单"),
    /** 余额不足 (原 OrderErrorCode.AMOUNT_LESS 2003)。 */
    AMOUNT_LESS(2003, "余额不足"),
    /** 订单状态异常 (原 OrderErrorCode.STATE_ERROR 2010)。 */
    STATE_ERROR(2010, "订单状态异常"),
    /** 未创建三方账户 (原 FinanceErrorCode.NOT_OPEN_ACCOUNT 2100)。 */
    NOT_OPEN_ACCOUNT(2100, "未创建三方账户"),
    /** 三方交易失败 (原 FinanceErrorCode.THIRD_TRADE_FAIL 2101)。 */
    THIRD_TRADE_FAIL(2101, "三方交易失败"),
    /** 小于最小提现金额 (原 ConfigErrorCode.LESS_THAN_MINIMUM_WITHDRAWAL_AMOUNT 4001)。 */
    LESS_THAN_MINIMUM_WITHDRAWAL_AMOUNT(4001, "小于最小提现金额"),
    /** 大于单日提现最高金额 (原 ConfigErrorCode.GREATER_THAN_MAXIMUM_DAILY_WITHDRAWAL_AMOUNT 4002)。 */
    GREATER_THAN_MAXIMUM_DAILY_WITHDRAWAL_AMOUNT(4002, "大于单日提现最高金额"),
    /** 小于最小充值金额 (原 ConfigErrorCode.LESS_THAN_MINIMUM_RECHARGE_AMOUNT 4003)。 */
    LESS_THAN_MINIMUM_RECHARGE_AMOUNT(4003, "小于最小充值金额"),
    ;

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态码对应说明文案
     */
    private final String message;
}
