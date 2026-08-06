package com.newzkl.platform.base.biz.auth.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.newzkl.platform.base.biz.auth.model.enums.AuthEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限关系表
 */
@Data
@TableName("auth_relations")
public class AuthRelationsDO extends BaseDO {

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
