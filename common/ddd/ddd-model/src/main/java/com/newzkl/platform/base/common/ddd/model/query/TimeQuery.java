package com.newzkl.platform.base.common.ddd.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author fang
 */
@Data
public class TimeQuery {
    /**
     * 类型 : 0 小时分组 1 天分组
     */
    @NotNull
    private Integer groupType;
    private Integer groupCount;
    /**
     * 开始时间
     */
    @NotNull
    private Long createBeginTime;
    /**
     * 结束时间
     */
    @NotNull
    private Long createEndTime;
}