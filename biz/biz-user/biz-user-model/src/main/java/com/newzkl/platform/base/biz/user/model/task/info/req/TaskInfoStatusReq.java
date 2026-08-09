package com.newzkl.platform.base.biz.user.model.task.info.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
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
    @NotNull
    private Long id;

    /**
     * 目标显示状态
     */
    @NotNull
    private CommonEnum.YesOrNo isShow;
}
