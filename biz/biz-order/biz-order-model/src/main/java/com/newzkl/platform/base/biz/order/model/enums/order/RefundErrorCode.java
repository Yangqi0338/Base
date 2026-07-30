package com.newzkl.platform.base.biz.order.model.enums.order;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 售后域错误码
 *
 * <p>迁移: 原 {@code com.zkl.scm.model.exception.goods.RefundErrorCode}, 实现平台统一 {@code ErrorCode} 接口。</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum RefundErrorCode implements ErrorCode {

    /** 订单非可售后状态, 无法售后。 */
    ORDER_STATE_CANNOT(2201, "订单非可售后状态, 无法售后"),
    /** 请先申请售后，再获取退货地址。 */
    OUT_ADDRESS_NOT_REFUND(2202, "请先申请售后，再获取退货地址"),
    /** 修改商品售后中数量失败。 */
    SKU_REFUNDING_COUNT(999, "修改商品售后中数量失败"),
    ;

    /** 状态码。 */
    private final Integer code;
    /** 状态码对应说明文案。 */
    private final String message;
}
