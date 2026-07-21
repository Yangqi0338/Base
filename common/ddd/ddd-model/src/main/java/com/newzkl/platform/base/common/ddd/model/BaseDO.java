package com.newzkl.platform.base.common.ddd.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础数据实体类
 *
 * @author god
 */
@Data
public class BaseDO extends BaseIdDO {

//    /**
//     * 创建人
//     *
//     */
//    @TableField(fill = FieldFill.INSERT)
//    protected String creator;
    /**
     * 创建时间
     *
     */
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 更新时间
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public void preInsert() {
        setId(null);
//        this.creator = null;
        this.createTime = null;
    }

    public void preUpdate() {
        this.updateTime = null;
//        this.updater = null;
    }

}
