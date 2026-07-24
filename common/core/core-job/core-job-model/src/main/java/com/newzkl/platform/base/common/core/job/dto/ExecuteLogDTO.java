package com.newzkl.platform.base.common.core.job.dto;

import com.newzkl.platform.base.common.core.job.model.ExecuteLogEnum;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 执行日志 DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExecuteLogDTO extends BaseDTO {
    private ExecuteLogEnum.BizType bizType;
    private String bizSourceCode;
    private Long userId;
    private Long bizId;
    private String jobHandler;
    private String jobName;
    private LocalDate scheduledDate;
    private LocalTime scheduledTime;
    private ExecuteLogEnum.Status status;
    private ExecuteLogEnum.Source source;
    private ExecuteLogEnum.Action action;
    private String msg;
    private LocalDateTime executeTime;

    /**
     * 计划执行时刻 = scheduledDate + scheduledTime
     *
     * @return 计划执行时刻, 任一为空返回 null
     */
    public LocalDateTime dueDateTime() {
        if (scheduledDate == null || scheduledTime == null) {
            return null;
        }
        return scheduledDate.atTime(scheduledTime);
    }
}
