package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Ignore;

import java.time.LocalDateTime;

/**
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
@Ignore
public class IdGeneratorDO extends BaseIdDO {
    private String generatorKey;

    private Long currentValue;

    private String description;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}