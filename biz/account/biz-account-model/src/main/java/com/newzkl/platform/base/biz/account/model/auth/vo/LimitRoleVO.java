package com.newzkl.platform.base.biz.account.model.auth.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色
 */
@Data
public class LimitRoleVO {

    private LocalDateTime createTime;
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
