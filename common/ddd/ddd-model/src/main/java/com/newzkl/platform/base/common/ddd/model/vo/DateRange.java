package com.newzkl.platform.base.common.ddd.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 赫兹
 */
@Data
public class DateRange {

    /**
     * 开始时间
     */
    private LocalDateTime begin;

    /**
     * 结束时间
     */
    private LocalDateTime end;

    /**
     * 当前时间的第几轮（段）
     */
    private Long turnNum;
}
