package com.newzkl.platform.base.biz.user.model.task.config.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 营销任务类型新增/修改请求
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskConfig.model.req.TaskConfigOperateReq}。</p>
 *
 * @author KC
 */
@Data
public class TaskConfigOperateReq {

    /**
     * 主键ID
     * @ext 新增留空，修改必填
     */
    private Long id;

    /**
     * 任务类型编码
     * @ext 1-观看激励广告，2-购买商品
     */
    @NotNull(message = "任务类型不能为空")
    private Integer taskType;

    /**
     * 任务类型名称
     */
    @NotBlank(message = "任务类型名称不能为空")
    private String taskTypeName;

    /**
     * 任务分组编码
     */
    @NotNull(message = "任务分组不能为空")
    private Integer taskGroup;

    /**
     * 任务分组名称
     */
    @NotBlank(message = "任务分组名称不能为空")
    private String taskGroupName;

    /**
     * 是否启用
     * @ext 1-是，0-否，默认启用
     */
    private Integer isEnabled = 1;

    /**
     * 备注信息
     */
    private String remark;
}
