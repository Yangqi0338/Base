package com.newzkl.platform.base.common.ddd.model.res;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.newzkl.platform.base.common.ddd.model.dto.ExecutorDTO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础出参实体类
 *
 * <p>结构与 BaseDO 一致: 操作人信息持 {@link ExecutorDTO} 对象, 由 {@code @JsonUnwrapped} 在
 * 序列化时平展为 creatorName/updater/updaterName, 对外 JSON 契约不变</p>
 *
 * @author god
 */
@Data
public class BaseRes implements Serializable {

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
    @JsonUnwrapped
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
