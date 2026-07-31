package com.newzkl.platform.base.biz.auth.model.rbac.vo;


import com.newzkl.platform.base.biz.auth.model.enums.AuthEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能点
 */
@Data
public class FunctionVO {
    /** 主键ID */
    private Long id;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口路径
     */
    private String urlPath;

    /**
     * 接口类型
     */
    private String urlMethod;

    /**
     * 类型
     *
     * @see AuthEnum.FunctionType
     */
    private String type;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改人id
     */
    private Long menderId;
}
