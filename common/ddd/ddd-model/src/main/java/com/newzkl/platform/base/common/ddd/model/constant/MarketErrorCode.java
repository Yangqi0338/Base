package com.newzkl.platform.base.common.ddd.model.constant;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MarketErrorCode
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum MarketErrorCode implements ErrorCode {

    /** NOT_EXIST_OR_STATE_ERROR. */
    NOT_EXIST_OR_STATE_ERROR(1104, "error1104"),
    /** MARKET_NOT_EXIST. */
    MARKET_NOT_EXIST(4101, "error4101"),
    /** MARKET_STATE_ERROR. */
    MARKET_STATE_ERROR(4102, "error4102"),
    /** PARAM_ERROR. */
    PARAM_ERROR(4103, "error4103"),
    ;

    private final Integer code;
    private final String message;
}
