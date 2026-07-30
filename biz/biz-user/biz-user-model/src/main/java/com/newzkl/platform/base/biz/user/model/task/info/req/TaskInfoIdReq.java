package com.newzkl.platform.base.biz.user.model.task.info.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 营销任务ID请求
 *
 * <p>迁移自旧 {@code TaskInfoController.TaskInfoIdReq} 内嵌静态类，按中台约定外提为独立 model。</p>
 *
 * @author KC
 */
@Data
public class TaskInfoIdReq {

    /**
     * 主键ID
     */
    @NotNull(message = "ID不能为空")
    @Positive(message = "ID必须为正整数")
    private Long id;
}
