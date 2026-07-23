package com.newzkl.platform.base.common.ddd.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 基础逻辑删除对象。
 *
 * <p>仅含主键与逻辑删除标记, 不含时间字段, 用于无需时序审计的关系/字典类表。</p>
 *
 * @author god
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class BaseLogicDO extends BaseIdDO {

    /**
     * 逻辑删除标记。
     * @ext 正常 0, 删除为 NULL(确保唯一索引生效)
     */
    @TableLogic(value = "0", delval = "NULL")
    @JsonIgnore
    protected Integer delFlag;
}
