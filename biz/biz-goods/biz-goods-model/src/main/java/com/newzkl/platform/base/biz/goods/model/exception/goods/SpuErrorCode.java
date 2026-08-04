package com.newzkl.platform.base.biz.goods.model.exception.goods;


import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/8/1714:52
 */
@Getter
@AllArgsConstructor
public enum SpuErrorCode implements ErrorCode {

    BUY_START_QTY(1102, "起购数量不足"),
    EXISTS(1103, "商品已存在"),
    NOT_EXIST_OR_STATE_ERROR(1104, "商品不存在或状态错误"),
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
