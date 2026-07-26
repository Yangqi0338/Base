package com.newzkl.platform.base.common.core.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础数据库 逻辑删除 对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseLogicDelDO extends BaseIdDO {

    /**
     * 逻辑删除
     * @ext 正常 0, 删除为 NULL(确保唯一索引生效)
     */
    @TableLogic(value = "0", delval = "NULL")
    @JsonIgnore
    protected Integer delFlag;
}
