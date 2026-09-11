package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCode;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程分类数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class CourseCategoryDO extends BaseDO {

    /**
     * 分类编码
     */
    @BusinessCode(BusinessType.COURSE_CATEGORY)
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
     * 排序值
     * @ext 越小越靠前
     */
    private Integer sort;

    /**
     * 该分类下课程总数
     */
    private Integer courseCount;

    /**
     * 是否启用
     */
    private CommonEnum.YesOrNo isEnabled;
}
