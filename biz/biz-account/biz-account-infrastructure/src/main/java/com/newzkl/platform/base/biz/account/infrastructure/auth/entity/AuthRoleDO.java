package com.newzkl.platform.base.biz.account.infrastructure.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色
 */
@Data
@TableName("auth_role")
public class AuthRoleDO {

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
