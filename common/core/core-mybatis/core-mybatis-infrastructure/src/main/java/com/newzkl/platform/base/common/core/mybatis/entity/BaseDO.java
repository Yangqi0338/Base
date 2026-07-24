package com.newzkl.platform.base.common.core.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import com.newzkl.platform.base.common.core.mybatis.SqlCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 数据实体基类
 * <p>包含主键ID、创建时间、更新时间</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseDO extends BaseIdDO {

    /**
     * 创建时间
     */
    @Index
    @NotNull(groups = SqlCommand.class)
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    @NotNull(groups = SqlCommand.class)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    /**
     * 预插入处理：清空 id、createTime、updateTime，由数据库或框架自动填充
     */
    public void preInsert() {
        setId(null);
        this.createTime = null;
        this.updateTime = null;
    }
}
