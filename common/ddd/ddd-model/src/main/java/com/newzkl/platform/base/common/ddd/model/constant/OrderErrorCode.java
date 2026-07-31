package com.newzkl.platform.base.common.ddd.model.constant;


import com.newzkl.platform.base.common.core.model.exception.ErrorCode;

/**
 * @author muc_fang
 * @Description: 订单异常信息
 * @date 2023/8/1714:52
 */
public enum OrderErrorCode implements ErrorCode {

    /**
     * 下单
     */
    NOT_SELECT(2001, "存在非选品商品:%s"),
    PARAM_CREATE_GOODS_DELIVER(2002, "存在超出配送区域的商品:%s"),
    AMOUNT_LESS(2003, "余额不足"),
    NOT_EXISTS(2004, "未找到订单"),
    /**
     * 流转
     */
    STATE_ERROR(2010, "订单状态异常"),
    REFUND_FAIL(2011, "售后失败:%s"),
    ;

    /**
     * 构造函数
     * @param code
     * @param message
     */
    OrderErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态码对应说明文案
     */
    private final String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
