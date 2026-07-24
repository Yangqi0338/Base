package com.newzkl.platform.base.biz.account.model.event;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 审核事件
 * @date 2023/7/2111:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent implements Serializable {
    /**
     * TAG
     */
    private String tag;
    /**
     * 审批ID
     */
    private Long flowId;
    /**
     * 模板ID
     */
    private Long templateId;
    /**
     * 申请人ID
     */
    private Long accountId;
    /**
     * 申请人角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 当前节点
     */
    private String code;
    /**
     * 审批动作 (0 未通过 1 通过)
     */
    private Integer action;
    /**
     * 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
     */
    private Integer state;
    /**
     * 审批人ID
     */
    private Long auditAccountId;
    /**
     * 最后拒绝原因: 状态变更未待用户提交前的最后一次拒绝原因
     */
    private String lastRefuseReason;
    /**
     * 审批单数据
     */
    private String data;
}
