package com.newzkl.platform.base.biz.user.model.pack.exception;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入会礼包订单错误码
 *
 * <p>迁移自旧 {@code com.zkl.scm.model.constants.admin.PackOrderErrorCode}，码值逐字保留。
 * 旧 {@code biz-account-model} 已有同名副本供升级校验用，本副本落 biz-user 侧避免跨 biz 引 model。</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum PackOrderErrorCode implements ErrorCode {

    /**
     * 订单已过期
     */
    ORDER_TIME_OUT(999, "订单已过期"),

    /**
     * 商品已下架
     */
    GOODS_DOWN(999, "商品已下架"),

    /**
     * 订单状态错误
     */
    STATE(999, "订单状态错误"),

    /**
     * 已是该等级
     */
    ALREADY_LEVEL(1504, "已是该等级"),

    /**
     * 礼包检查失败
     */
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
