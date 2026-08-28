package com.newzkl.platform.base.biz.auth.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.TableIndex;
import org.dromara.autotable.annotation.TableIndexes;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;

/**
 * 权限关系持久化对象
 *
 * <p>account-role / role-permission / account-permission 三方多态关联单表。
 * 端隔离: 每条关系归属一个 client, 唯一键含 client, 保证跨端同 id 关系不冲突, 账号只与同端角色/权限绑定</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
@TableIndexes({
        @TableIndex(name = "key", type = IndexTypeEnum.UNIQUE, fields = {"client", "type", "source", "target", "delFlag"}),
        @TableIndex(name = "source", fields = {"client", "type", "source"}),
        @TableIndex(name = "target", fields = {"client", "type", "target"})
})
public class PermissionRelationDO extends BaseDO {

    /**
     * 所属端
     */
    private AccountEnum.Client client;

    /**
     * 关系类型
     */
    private PermissionEnum.RelationType type;

    /**
     * 源对象标识
     * @ext account 侧存账号 id 字符串, role 侧存角色 code
     */
    private String source;

    /**
     * 目标对象标识
     * @ext role 侧存角色 code, permission 侧存权限 id 字符串
     */
    private String target;

    /**
     * 关系来源
     */
    private PermissionEnum.Source origin;
}
