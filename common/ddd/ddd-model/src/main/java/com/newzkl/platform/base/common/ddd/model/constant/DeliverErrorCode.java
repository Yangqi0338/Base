package com.newzkl.platform.base.common.ddd.model.constant;


import com.newzkl.platform.base.common.core.model.exception.ErrorCode;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/8/1714:52
 */
public enum DeliverErrorCode implements ErrorCode {

    ORDER_STATE_CANNOT(2101, "非待发货状态, 无法发货, 订单ID : %s"),
    SKU_NOT(2102, "该商品未购买过"),
    /**
     * 一般异常 999
     */
    DELIVER_OVER(999, "发货数量超出! SKU_ID : %s"),
    UPDATE_DELIVER_NUM(999, "更新发货数量失败! SKU_ID : %s");

    /**
     * 构造函数
     * @param code
     * @param message
     */
    DeliverErrorCode(Integer code, String message) {
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
