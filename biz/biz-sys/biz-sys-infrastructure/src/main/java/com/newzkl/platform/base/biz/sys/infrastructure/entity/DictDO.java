package com.newzkl.platform.base.biz.sys.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 字典数据对象
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dict")
public class DictDO extends BaseDO {

    /**
     * 字典值
     */
    @Index
    private String value;

    /**
     * 字典描述
     */
    @TableField("`desc`")
    private String desc;
}
