package com.newzkl.platform.base.common.core.job.secondLevel;

import com.zhongze.chicken.common.enums.admin.ExecuteLogEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 执行日志 VO
 */
@Data
public class ExecuteLogVO extends BaseVO {
    private ExecuteLogEnum.BizType bizType;
    private String bizSourceCode;
    private Long userId;
    private String userNickname;
    private String userPhone;
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
}
