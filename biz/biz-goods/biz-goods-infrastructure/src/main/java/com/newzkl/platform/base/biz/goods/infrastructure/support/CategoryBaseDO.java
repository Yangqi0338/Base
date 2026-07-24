package com.newzkl.platform.base.biz.goods.infrastructure.support;

import com.baomidou.mybatisplus.annotation.TableField;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.biz.goods.model.enums.CommonEnum;
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
    private Long accountId;
    /**
     * 名称 查询
     */
    private String name;
    /**
     * 描述
     */
    @TableField("`desc`")
    private String desc;
    /**
     * 图片
     */
    private String img;
    /**
     * 排序
     */
    private Integer idx;
    /**
     * 是否启用
     */
    private CommonEnum.YesOrNo isEnabled;
}
