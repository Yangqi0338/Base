package com.newzkl.platform.base.biz.course.model.category.res;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程分类出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.res.CourseCategoryRes}。
 * {@code id}/{@code createTime}/{@code updateTime} 继承自 {@code BaseRes}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseCategoryRes extends BaseRes {

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 小标题
     */
    private String subTitle;

    /**
     * 排序值, 值越小越靠前
     */
    private Integer sort;

    /**
     * 该分类下课程数量, 不分状态
     */
    private Integer courseCount;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    private CommonEnum.YesOrNo isEnabled;

    /**
     * 启用状态描述: 启用/禁用
     */
    private String isEnabledDesc;
}
