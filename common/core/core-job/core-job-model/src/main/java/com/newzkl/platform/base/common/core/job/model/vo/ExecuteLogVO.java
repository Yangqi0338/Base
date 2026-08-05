package com.newzkl.platform.base.common.core.job.model.vo;

import com.newzkl.platform.base.common.core.job.model.model.ExecuteLogEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 执行日志 VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExecuteLogVO extends BaseRes {
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
