package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.user.model.task.enums.TaskTypeEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 营销任务(task_info)持久化对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.TaskInfoDO}（表 {@code task_info}）。</p>
 *
 * <p>迁移说明：中台 {@code DynamicTableNameInnerInterceptor} 按类名去 {@code DO} 后缀推表名，
 * {@code TaskInfoDO} → {@code task_info}，故不写显式表名；旧 {@code @TableField} 逐字段声明
 * 由 MyBatis-Plus 下划线策略替代；旧 {@code is_deleted} 逻辑删除由 {@code BaseDO} 的
 * {@code delFlag} 统一承接。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class TaskInfoDO extends BaseDO {

    /**
     * 任务编号
     */
    @Index
    private String taskNum;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务配置ID
     * @ext 关联 task_config 主键
     */
    @Index
    private Long taskId;

    /**
     * 任务类型编码
     */
    private TaskTypeEnum taskType;

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
     * @ext 最多200字
     */
    private String taskIntro;

    /**
     * 完整观看广告数
     * @ext 仅观看激励广告类型有效
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
     * 是否显示
     */
    private CommonEnum.YesOrNo isShow;
}
