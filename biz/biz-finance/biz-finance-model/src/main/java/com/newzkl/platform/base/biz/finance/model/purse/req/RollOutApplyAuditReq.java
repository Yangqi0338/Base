package com.newzkl.platform.base.biz.finance.model.purse.req;

import lombok.Data;

/**
 * @author niu
 * @description: 转出申请审核
 * @date 2023/12/23 13:56
 */
@Data
public class RollOutApplyAuditReq {

    /**
     * 转出申请id
     */
    private Long rollOutApplyId;

    /**
     * 审核状态 1：审核成功  2：审核失败
     */
    private Integer auditSate;

    /**
     * 备注
     */
    private String remark;
}
