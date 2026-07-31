package com.newzkl.platform.base.common.ddd.model.vo;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础视图实体类
 *
 * @author god
 */
@Setter
@Getter
public class BaseVO implements Serializable {

    /**
     * 主键
     */
    protected Long id;

    /** 创建时间 */
    protected LocalDateTime createTime;

    /** 更新时间 */
    protected LocalDateTime updateTime;
}
