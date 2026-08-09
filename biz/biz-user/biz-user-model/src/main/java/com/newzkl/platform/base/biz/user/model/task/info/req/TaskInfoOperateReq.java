package com.newzkl.platform.base.biz.user.model.task.info.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 营销任务新增/修改请求
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskInfo.model.req.TaskInfoOperateReq}。</p>
 *
 * @author KC
 */
@Data
public class TaskInfoOperateReq {

    /**
     * 主键ID
     * @ext 新增时为空，修改时必填
     */
    private Long id;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    @Size(max = 128, message = "任务名称长度不能超过128字")
    private String taskName;

    /**
     * 任务配置ID
     * @ext 关联 task_config 主键
     */
    @NotNull(message = "任务类型ID不能为空")
    private Long taskId;

    /**
     * 任务开始时间
     */
    @NotNull(message = "任务开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 任务结束时间
     */
    @NotNull(message = "任务结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 任务简介
     * @ext 最多200字
     */
    @Size(max = 200, message = "任务简介长度不能超过200字")
    private String taskIntro;

    /**
     * 完整观看广告数
     * @ext 仅观看激励广告类型必填
     */
    private Integer adCount;

    /**
     * 参与活动的商品集合
     */
    private String goodsList;

    /**
     * 是否显示
     * @ext 1-是，0-否，默认显示
     */
    private Integer isShow = 1;
}
