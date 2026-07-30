package com.newzkl.platform.base.biz.user.model.task.config.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 营销任务类型启用/禁用请求
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskConfig.model.req.TaskConfigStatusReq}。</p>
 *
 * @author KC
 */
@Data
public class TaskConfigStatusReq {

    /**
     * 主键ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 目标状态（1-启用，0-禁用）
     */
    @NotNull(message = "状态不能为空")
    private Integer isEnabled;
}
