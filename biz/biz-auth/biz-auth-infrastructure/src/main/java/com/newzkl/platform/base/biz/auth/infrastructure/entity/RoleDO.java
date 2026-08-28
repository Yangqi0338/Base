package com.newzkl.platform.base.biz.auth.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.TableIndex;
import org.dromara.autotable.annotation.TableIndexes;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;

/**
 * 角色持久化对象
 *
 * <p>RBAC 权限分组, (client, code) 端内唯一, 通过 permission_relation 绑定账号与权限。
 * 端隔离: 各端角色独立, code 仅在同端内唯一</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
@TableIndexes({
        @TableIndex(name = "key", type = IndexTypeEnum.UNIQUE, fields = {"client", "code", "delFlag"})
})
public class RoleDO extends BaseDO {

    /**
     * 所属端
     */
    private AccountEnum.Client client;

    /**
     * 角色编码
     */
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
