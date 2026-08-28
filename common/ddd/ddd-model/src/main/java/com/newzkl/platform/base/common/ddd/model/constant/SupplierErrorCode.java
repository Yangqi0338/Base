package com.newzkl.platform.base.common.ddd.model.constant;

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
public enum SupplierErrorCode implements ErrorCode {

    OVER_INDUSTRY(999, "超过最大行业数量."),
    AUDIT_STATE(998, "供应商审核状态不允许重复提交."),
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
