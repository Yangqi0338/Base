package com.newzkl.platform.base.biz.sys.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 字典条目数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class DictItemDO extends BaseDO {

    /**
     * 父字典 id
     */
    @Index
    private Long dictId;

    /**
     * 条目键
     */
    @Index
    private String itemKey;

    /**
     * 条目值
     */
    private String itemValue;

    /**
     * 排序
     * @ext 升序
     */
    @Index
    private Integer sort;
}
