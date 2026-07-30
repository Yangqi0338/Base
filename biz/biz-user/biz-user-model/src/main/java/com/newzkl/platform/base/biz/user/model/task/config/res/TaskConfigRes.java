package com.newzkl.platform.base.biz.user.model.task.config.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销任务类型出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskConfig.model.dto.TaskConfigDTO}。
 * id/createTime/updateTime 由 {@code BaseRes} 提供。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskConfigRes extends BaseRes {

    /**
     * 任务类型编码
     */
    private Integer taskType;

    /**
     * 任务类型名称
     */
    private String taskTypeName;

    /**
     * 任务分组编码
     */
    private Integer taskGroup;

    /**
     * 任务分组名称
     */
    private String taskGroupName;

    /**
     * 任务数量（关联的任务条数）
     */
    private Integer taskCount;

    /**
     * 是否启用（1-是，0-否）
     */
    private Integer isEnabled;

    /**
     * 备注信息
     */
    private String remark;
}
