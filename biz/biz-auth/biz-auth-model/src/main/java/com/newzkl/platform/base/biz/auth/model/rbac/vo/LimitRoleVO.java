package com.newzkl.platform.base.biz.auth.model.rbac.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色
 */
@Data
public class LimitRoleVO {

    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;

    /**
     * id
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 描述
     */
    private String des;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改人id
     */
    private Long menderId;

}
