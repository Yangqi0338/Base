package com.newzkl.platform.base.biz.auth.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.auth.model.enums.RelationEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.TableIndex;
import org.dromara.autotable.annotation.TableIndexes;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;

/**
 * 权限关系持久化对象
 *
 * <p>account-role / role-permission / account-permission 三方多态关联单表</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("permission_relation")
@TableIndexes({
        @TableIndex(name = "idx_key", type = IndexTypeEnum.UNIQUE, fields = {"type", "source_id", "target_id"}),
        @TableIndex(name = "idx_source", fields = {"type", "source_id"}),
        @TableIndex(name = "idx_target", fields = {"type", "target_id"}),
})
public class PermissionRelationDO extends BaseDO {

    /**
     * 关系类型
     */
    private RelationEnum.Type type;

    /**
     * 源对象ID
     * @ext account 或 role
     */
    private Long sourceId;

    /**
     * 目标对象ID
     * @ext role 或 permission
     */
    private Long targetId;

    /**
     * 关系来源
     */
    private RelationEnum.Source source;
}
