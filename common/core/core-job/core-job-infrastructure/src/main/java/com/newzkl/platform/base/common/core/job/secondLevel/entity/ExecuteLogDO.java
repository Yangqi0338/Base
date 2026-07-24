package com.newzkl.platform.base.common.core.job.secondLevel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.job.model.ExecuteLogEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 执行日志
 * @ext PENDING 调度计划 + 执行流水
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("execute_log")
public class ExecuteLogDO extends BaseDO {

    /**
     * 业务类型
     * @ext 首版仅 ASSET
     */
    @Index
    @NotNull
    private ExecuteLogEnum.BizType bizType;

    /**
     * 业务对象来源 code
     */
    @Index
    @NotNull
    @Size(max = 50)
    private String bizSourceCode;

    /**
     * 业务对象归属用户ID
     * @ext 计划创建时回填, 便于按用户筛选
     */
    @Index
    @NotNull
    private Long userId;

    /**
     * 动作产生的对象 id
     * @ext 执行后回填
     */
    @Index
    private Long bizId;

    /**
     * XXL-Job handler 英文名
     * @ext dispatcher switch / 跳转 XXL-Job 详情用
     */
    @Index
    @NotNull
    @Size(max = 50)
    private String jobHandler;

    /**
     * 运营展示中文名
     */
    @NotNull
    @Size(max = 50)
    private String jobName;

    /**
     * 计划执行日期
     */
    @NotNull
    private LocalDate scheduledDate;

    /**
     * 计划执行时刻
     */
    @Index
    @NotNull
    private LocalTime scheduledTime;

    /**
     * 状态
     */
    @Index
    @NotNull
    private ExecuteLogEnum.Status status;

    /**
     * 来源
     */
    @NotNull
    private ExecuteLogEnum.Source source;

    /**
     * 动作
     */
    @NotNull
    private ExecuteLogEnum.Action action;

    /**
     * 操作说明 / 失败原因 / SKIP 原因
     */
    @ColumnType(value = MysqlTypeConstant.TEXT)
    private String msg;

    /**
     * 实际执行完成时间
     */
    @Index
    private LocalDateTime executeTime;
}
