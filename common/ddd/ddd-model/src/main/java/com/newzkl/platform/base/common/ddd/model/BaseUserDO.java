package com.newzkl.platform.base.common.ddd.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 基础数据实体类
 *
 * @author god
 */
@Data
public class BaseUserDO extends BaseDO {

    /**
     * 创建人
     *
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long creator;

    /**
     * 更新人
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Long updater;

    @Override
    public void preInsert() {
        super.preInsert();
        this.creator = null;
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        this.updater = null;
    }

}
