package com.newzkl.platform.base.common.ddd.model.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础数据传输对象
 *
 * <p>结构与 BaseDO 一致: 操作人信息持 {@link ExecutorDTO} 对象, 免去平铺字段与嵌套结构的来回转换</p>
 */
@Setter
@Getter
public class BaseDTO implements Serializable {

    /**
     * 主键
     */
    protected Long id;

    /**
     * 创建人id
     */
    protected Long creatorId;

    /**
     * 操作人信息
     */
    protected ExecutorDTO executor;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;
}
