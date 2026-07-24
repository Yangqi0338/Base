package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

/**
 * 门店分类 #store
 *
 * @author fang
 */
@Data
@TableName("store_category")
public class StoreCategoryDO extends BaseDO {

    /**
     * 门店名称
     */
    private String name;

    /**
     * 图标
     */
    private String logo;

    /**
     * 排序
     */
    @TableField("`index`")
    private Integer index;
}