package com.newzkl.platform.base.biz.account.model.exception;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/7/2816:16
 */
@Getter
@AllArgsConstructor
public enum PackOrderErrorCode implements ErrorCode {

    ORDER_TIME_OUT(999, "订单已过期"),
    GOODS_DOWN(999, "商品已下架"),
    STATE(999, "订单状态错误"),
    ALREADY_LEVEL(1504, "已是该等级"),
    LEVEL_UP_ERROR(1505, "礼包检查失败"),
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
