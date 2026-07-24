package com.newzkl.platform.base.common.ddd.model.dto;


import com.newzkl.platform.base.common.core.model.enums.job.ExecuteLogEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 执行日志 DTO
 */
@Data
public class ExecuteLogDTO extends BaseDTO {
    private Long id;
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
     */
    public LocalDateTime dueDateTime() {
        if (scheduledDate == null || scheduledTime == null) return null;
        return scheduledDate.atTime(scheduledTime);
    }
}
