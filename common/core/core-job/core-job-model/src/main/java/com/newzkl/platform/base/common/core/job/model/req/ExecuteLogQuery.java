package com.newzkl.platform.base.common.core.job.model.req;

import com.newzkl.platform.base.common.core.job.model.model.ExecuteLogEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 执行日志查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExecuteLogQuery extends PageQuery {

    /** 业务类型 */
    private ExecuteLogEnum.BizType bizType;
    /**
     * 业务对象来源 code
     * @ext 等值
     */
    private String bizSourceCode;
    /** XXL-Job handler 英文名 */
    private String jobHandler;
    /** 状态 */
    private ExecuteLogEnum.Status status;
    /** 来源 */
    private ExecuteLogEnum.Source source;
    /** 计划执行日期起 */
    private LocalDate scheduledDateStart;
    /** 计划执行日期止 */
    private LocalDate scheduledDateEnd;
    /** 创建时间起 */
    private LocalDateTime createTimeStart;
    /** 创建时间止 */
    private LocalDateTime createTimeEnd;
    /**
     * 用户名
     * @ext 模糊, 前端入参; app 层转 userIds 落库
     */
    private String userNickname;
    /**
     * 手机号
     * @ext 模糊, 前端入参; app 层转 userIds 落库
     */
    private String userPhone;
    /**
     * 用户ID列表
     * @ext IN 等值, 由 app 层从 userNickname/userPhone 解析填入; 前端无需传
     */
    private List<Long> userIds;
}
