package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autotable.annotation.UniqueIndex;

import java.time.LocalDateTime;

/**
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class IdGeneratorDO extends BaseIdDO {
    /**
     * 发号器 key(= BusinessType.name())
     */
    @UniqueIndex
    private String generatorKey;

    /**
     * 已分配到的最大序列值
     */
    private Long currentValue;

    private String description;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}