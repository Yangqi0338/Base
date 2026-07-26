package com.newzkl.platform.base.biz.sys.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台角色数据对象。
 *
 * <p>迁移说明: 旧表 {@code arole} 有 {@code admin_count} 冗余列, 新设计将其视为
 * 派生统计 (由关联账号实时统计), 不再作为持久化字段, 故 {@code adminCount} 只在
 * {@code AroleRes} 承载, 此处以 {@code exist = false} 声明。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("arole")
public class AroleDO extends BaseDO {

    /**
     * 角色名称。
     */
    private String name;

    /**
     * 备注。
     */
    @TableField("`comment`")
    private String comment;

    /**
     * 关联账号数量 (派生统计, 非持久化列)。
     */
    @TableField(exist = false)
    private Integer adminCount;
}
