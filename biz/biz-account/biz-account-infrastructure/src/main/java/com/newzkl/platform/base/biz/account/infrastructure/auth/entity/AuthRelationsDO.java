package com.newzkl.platform.base.biz.account.infrastructure.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限关系表
 */
@Data
@TableName("auth_relations")
public class AuthRelationsDO {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private LocalDateTime createTime;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 类型
     * @see AuthEnum.RelationType
     */
    private String type;

    /**
     * 主体ID
     */
    private Long sourceId;
    /**
     * 客体ID
     */
    private Long targetId;

}
