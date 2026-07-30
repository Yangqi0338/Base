package com.newzkl.platform.base.biz.user.model.task.config.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 营销任务类型ID请求（删除/详情用）
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskConfig.model.req.TaskConfigIdReq}。</p>
 *
 * @author KC
 */
@Data
public class TaskConfigIdReq {

    /**
     * 主键ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;
}
