package com.newzkl.platform.base.common.ddd.facade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 审批事件
 *
 * <p>plugin-audit 审批流转到终态时外发的事件契约, 与插件侧 AuditEventMsg 字段对齐,
 * 经本地消息表投递, 业务方消费后执行终态动作</p>
 *
 * @author KC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditEvent implements Serializable {

    /** 审批流主键 */
    private Long id;

    /** 申请人账号主键 */
    private Long accountId;

    /** 申请人账号名称 */
    private String username;

    /** 审批模板主键 */
    private Long templateId;

    /** 审批状态 1 审批中 2 通过 3 拒绝 4 终止 */
    private Integer state;

    /** 事件路由标签 */
    private String tag;

    /** 业务数据 JSON */
    private String data;

    /** 最后拒绝原因 */
    private String lastRefuseReason;
}
