package com.newzkl.platform.base.biz.goods.model.goods.req.audit;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 审批结果请求对象
 */
@Data
public class ApprovalResultReq implements Serializable {

    /**
     * 审批ID
     */
    private Long id;

    /**
     * 审批状态
     *
     * @see AuditEnum.State
     */
    private String state;

    /**
     * 原因
     */
    private String reason;

    /**
     * 修改参数
     * @ext JSON格式, 具体格式约定
     */
    private String editCommand;

}
