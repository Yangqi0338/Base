package com.newzkl.platform.base.common.ddd.model.res;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础出参实体类
 *
 * <p>平铺主键, 操作人信息与时间字段, 与 BaseDO 的嵌套 executor 结构对应</p>
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
     * 创建人名称
     */
    protected String creatorName;

    /**
     * 更新人 ID
     */
    protected Long updater;

    /**
     * 更新人名称
     */
    protected String updaterName;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;
}
