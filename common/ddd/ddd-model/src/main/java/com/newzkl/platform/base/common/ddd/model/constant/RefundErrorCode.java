package com.newzkl.platform.base.common.ddd.model.constant;


import com.newzkl.platform.base.common.core.model.exception.ErrorCode;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/8/1714:52
 */
public enum RefundErrorCode implements ErrorCode {

    ORDER_STATE_CANNOT(2201, "订单非可售后状态, 无法售后"),
    OUT_ADDRESS_NOT_REFUND(2202, "请先申请售后，再获取退货地址"),
    /**
     * 一般异常 999
     */
    SKU_REFUNDING_COUNT(999, "修改商品售后中数量失败"),
    ;
    /**
     * 构造函数
     * @param code
     * @param message
     */
    RefundErrorCode(Integer code, String message) {
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
