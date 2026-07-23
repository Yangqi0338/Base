package com.newzkl.platform.base.biz.goods.model.exception.user;

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
public enum AuditErrorCode implements ErrorCode {

    AUDIT_TEMPLATE_CONFIG_ERROR(999, "审批模板配置异常"),
    AUDIT_DATA_LOSE(999, "审批流关联数据丢失"),
    EXIST_AUDIT(999, "审批已存在"),
    AUDIT_FINISHED(999, "审批已完结"),
    NOT_FOUND(999, "审批单不存在"),
    EDIT_BUSINESS_DATA(999, "修改业务数据失败"),
    FLOW_CODE_ERROR(999, "流转节点失败"),
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
