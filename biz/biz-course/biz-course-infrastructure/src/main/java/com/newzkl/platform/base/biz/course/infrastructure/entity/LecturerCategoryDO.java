package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 讲师分类数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class LecturerCategoryDO extends BaseDO {

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 是否启用
     */
    private CommonEnum.YesOrNo isEnabled;
}
