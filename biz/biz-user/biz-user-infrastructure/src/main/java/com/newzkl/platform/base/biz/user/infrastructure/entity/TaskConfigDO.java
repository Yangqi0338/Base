package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.user.model.task.enums.TaskTypeEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 营销任务类型(task_config)持久化对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.TaskConfigDO}（表 {@code task_config}）。</p>
 *
 * <p>迁移说明：中台 {@code DynamicTableNameInnerInterceptor} 按类名去 {@code DO} 后缀推表名，
 * {@code TaskConfigDO} → {@code task_config}，故不写显式表名；旧 {@code @TableField} 逐字段声明
 * 由 MyBatis-Plus 下划线策略替代。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class TaskConfigDO extends BaseDO {

    /**
     * 任务类型
     */
    private TaskTypeEnum taskType;

    /**
     * 任务分组编码
     */
    @Index
    private Integer taskGroup;

    /**
     * 任务分组名称
     */
    private String taskGroupName;

    /**
     * 任务数量
     * @ext 关联的任务条数
     */
    private Integer taskCount;

    /**
     * 是否启用
     */
    private CommonEnum.YesOrNo isEnabled;

    /**
     * 备注信息
     */
    private String remark;
}
