package com.newzkl.platform.base.common.ddd.model.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础数据传输对象，包含主键 ID 和时间字段
 */
@Setter
@Getter
public class BaseDTO implements Serializable {

    /**
     * 主键
     */
    protected Long id;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;
}
