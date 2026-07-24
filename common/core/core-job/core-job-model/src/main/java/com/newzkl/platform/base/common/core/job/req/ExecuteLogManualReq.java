package com.newzkl.platform.base.common.core.job.req;

import com.newzkl.platform.base.common.core.job.model.ExecuteLogEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 手动新增 ExecuteLog PENDING 入参
 */
@Data
public class ExecuteLogManualReq {

    /** 业务类型 */
    @NotNull
    private ExecuteLogEnum.BizType bizType;

    /** 业务对象来源 code */
    @NotBlank
    private String bizSourceCode;

    /** XXL-Job handler 英文名 */
    @NotBlank
    private String jobHandler;

    /** 任务展示名 */
    @NotBlank
    private String jobName;

    /** 计划执行日期 */
    private LocalDate scheduledDate;

    /** 计划执行时刻 */
    private LocalTime scheduledTime;

    /** 备注 */
    private String msg;

    /**
     * 是否立即执行
     * @ext 插入后直接反射触发一次
     */
    private Boolean immediate = false;
}
