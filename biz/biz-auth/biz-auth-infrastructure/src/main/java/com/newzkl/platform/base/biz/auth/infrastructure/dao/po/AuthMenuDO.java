package com.newzkl.platform.base.biz.auth.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单
 */
@Data
@TableName("auth_menu")
public class AuthMenuDO {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 隶属菜单id
     */
    private Long affiliatedMenuId;

    /**
     * 菜单名称
     */
    private String name;
    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改人id
     */
    private Long menderId;

    /**
     * 排序值
     */
    private Integer sort;

}
