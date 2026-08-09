package com.newzkl.platform.base.biz.finance.model.purse.req;

import lombok.Data;

/**
 * 转出申请审核请求
 *
 * @author niu
 * @date 2023/12/23 13:56
 */
@Data
public class RollOutApplyAuditReq {

    /**
     * 转出申请id
     */
    private Long rollOutApplyId;

    /**
     * 审核状态
     * @ext 1 审核成功 2 审核失败; 无对应枚举, 保留 Integer
     */
    private Integer auditSate;

    /**
     * 备注
     */
    private String remark;
}
