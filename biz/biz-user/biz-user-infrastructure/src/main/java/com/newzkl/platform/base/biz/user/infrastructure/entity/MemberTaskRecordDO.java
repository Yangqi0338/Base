package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.user.model.task.enums.TaskStatusEnum;
import com.newzkl.platform.base.biz.user.model.task.enums.TaskTypeEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 会员任务进度(member_task_record)持久化对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class MemberTaskRecordDO extends BaseDO {

    /**
     * 会员ID
     */
    @Index
    private String memberId;

    /**
     * 会员昵称
     */
    private String memberNickname;

    /**
     * 任务编号
     * @ext 关联 task_info.task_num
     */
    @Index
    private String taskNum;

    /**
     * 任务配置ID
     * @ext 关联 task_config 主键
     */
    private Long taskConfigId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型编码
     */
    private TaskTypeEnum taskType;

    /**
     * 任务状态编码
     */
    private TaskStatusEnum taskStatus;

    /**
     * 任务完成次数或金额
     */
    private Long count;

    /**
     * 完成条件
     */
    private String completeCondition;

    /**
     * 任务进度
     */
    private String taskProgress;

    /**
     * 红包奖励
     * @ext 单位 0.0001 元，1元存 10000
     */
    private Long redPacketReward;
}
