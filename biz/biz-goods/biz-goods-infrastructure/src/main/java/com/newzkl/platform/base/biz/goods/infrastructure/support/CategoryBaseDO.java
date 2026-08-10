package com.newzkl.platform.base.biz.goods.infrastructure.support;

import com.baomidou.mybatisplus.annotation.TableField;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础数据实体类
 *
 * @author god
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class CategoryBaseDO extends BaseDO {
    /**
     * 专属人
     */
    protected Long accountId;
    /**
     * 名称
     * @ext 查询
     */
    protected String name;
    /**
     * 描述
     */
    @TableField("`desc`")
    protected String desc;
    /**
     * 图片
     */
    protected String img;
    /**
     * 排序
     */
    protected Integer idx;
    /**
     * 是否启用
     */
    protected CommonEnum.YesOrNo isEnabled;
}
