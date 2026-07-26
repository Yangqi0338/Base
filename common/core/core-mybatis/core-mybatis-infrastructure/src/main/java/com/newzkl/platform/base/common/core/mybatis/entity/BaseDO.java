package com.newzkl.platform.base.common.core.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.common.ddd.model.dto.ExecutorDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;

/**
 * 数据实体基类
 * <p>包含主键ID、创建时间、更新时间</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseDO extends BaseIdDO {

    /**
     * 操作人信息。
     * @ext creator/updater 合并, JSON 列, 含 id 与名称
     */
    @JsonSerializable
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected ExecutorDTO executor;

    /**
     * 创建时间。
     */
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 更新时间。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    /**
     * 逻辑删除标记。
     * @ext 正常 0, 删除为 NULL(确保唯一索引生效)
     */
    @TableLogic(value = "0", delval = "NULL")
    @JsonIgnore
    protected Integer delFlag;

    /**
     * 预插入清理。
     */
    public void preInsert() {
        setId(null);
        this.createTime = null;
        this.updateTime = null;
    }

    /**
     * 预更新清理。
     */
    public void preUpdate() {
        this.updateTime = null;
    }
}
