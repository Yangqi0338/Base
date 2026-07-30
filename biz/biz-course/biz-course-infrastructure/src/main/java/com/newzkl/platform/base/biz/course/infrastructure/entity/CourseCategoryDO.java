package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程分类数据对象
 *
 * <p>迁移自 {@code com.zkl.scm.user.infrastructure.entity.CourseCategoryDO}(表 {@code course_category})。
 * 偏离说明: 源逻辑删除列为 {@code is_deleted}(0/1), Base 统一由 {@link BaseDO#getDelFlag}
 * 承担({@code del_flag}, 正常 0 / 删除 NULL), 故不再声明 {@code is_deleted};
 * 源 {@code createBy}/{@code updateBy} 由 {@link BaseDO#getExecutor} 承担。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("course_category")
public class CourseCategoryDO extends BaseDO {

    /**
     * 分类编码, KF 前缀自编码
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
     * 排序值, 越小越靠前
     */
    private Integer sort;

    /**
     * 该分类下课程总数, 不分状态
     */
    private Integer courseCount;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    private Integer isEnabled;
}
