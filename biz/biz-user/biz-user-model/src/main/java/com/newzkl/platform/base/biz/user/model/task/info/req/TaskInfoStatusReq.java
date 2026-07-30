package com.newzkl.platform.base.biz.user.model.task.info.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 营销任务显示状态切换请求
 *
 * <p>迁移自旧 {@code TaskInfoController.TaskInfoStatusReq} 内嵌静态类，按中台约定外提为独立 model。</p>
 *
 * @author KC
 */
@Data
public class TaskInfoStatusReq {

    /**
     * 主键ID
     */
    @NotNull(message = "ID不能为空")
    @Positive(message = "ID必须为正整数")
    private Long id;

    /**
     * 目标显示状态（1-显示，0-隐藏）
     */
    @NotNull(message = "显示状态不能为空")
    private Integer isShow;
}
