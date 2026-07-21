package com.newzkl.platform.base.common.ddd.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 基础数据库 逻辑删除 对象
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class BaseLogicDelDO extends BaseDO {

    /*
     * 逻辑删除 正常 0, 删除为 NULL(确保唯一索引生效)
     * */
    @TableLogic(value = "0", delval = "NULL")
    @JsonIgnore
    protected Integer delFlag;
}
