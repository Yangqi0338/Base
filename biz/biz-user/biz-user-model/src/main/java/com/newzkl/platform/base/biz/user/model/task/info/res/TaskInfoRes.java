package com.newzkl.platform.base.biz.user.model.task.info.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 营销任务出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskInfo.model.vo.TaskInfoVO}。
 * id/createTime/updateTime 由 {@code BaseRes} 提供。</p>
 *
 * <p>迁移说明：旧 VO 无 {@code taskTypeName}，但旧 {@code TaskInfoDO} 与领域对象均有该列，
 * 且 create/update 会由任务类型回填，故出参补齐。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInfoRes extends BaseRes {

    /**
     * 任务编号
     */
    private String taskNum;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务配置ID（关联 task_config 主键）
     */
    private Long taskId;

    /**
     * 任务类型编码
     */
    private Integer taskType;

    /**
     * 任务类型名称
     */
    private String taskTypeName;

    /**
     * 任务开始时间
     */
    private LocalDateTime startTime;

    /**
     * 任务结束时间
     */
    private LocalDateTime endTime;

    /**
     * 任务简介
     */
    private String taskIntro;

    /**
     * 完整观看广告数
     */
    private Integer adCount;

    /**
     * 参与活动的商品集合
     */
    private String goodsList;

    /**
     * 完成人数
     */
    private Integer completeCount;

    /**
     * 是否显示（1-是，0-否）
     */
    private Integer isShow;
}
