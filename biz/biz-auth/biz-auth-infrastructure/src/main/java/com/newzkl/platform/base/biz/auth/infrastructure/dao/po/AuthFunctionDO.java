package com.newzkl.platform.base.biz.auth.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.newzkl.platform.base.biz.auth.model.enums.AuthEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能点
 */
@Data
@TableName("auth_function")
public class AuthFunctionDO {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private LocalDateTime createTime;
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
