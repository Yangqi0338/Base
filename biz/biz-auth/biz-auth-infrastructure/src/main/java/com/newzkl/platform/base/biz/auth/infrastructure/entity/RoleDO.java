package com.newzkl.platform.base.biz.auth.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;

/**
 * 角色持久化对象
 *
 * <p>RBAC 权限分组, code 唯一, 通过 permission_relation 绑定账号与权限</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("role")
public class RoleDO extends BaseDO {

    /**
     * 角色编码
     */
    @Index(type = IndexTypeEnum.UNIQUE)
    private String code;

    /**
     * 角色名称
     */
    @Index
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 排序
     */
    @Index
    private Integer sort;
}
