package com.newzkl.platform.base.biz.course.model.lecturercategory.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 讲师分类分页查询
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.req.LecturerCategoryPageReq}
 * (源直接继承 mybatis-plus {@code Page}, 新架构统一继承 {@code BizPageQuery})。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerCategoryQuery extends BizPageQuery {

    /**
     * 分类名称, 模糊查询
     */
    private String categoryName;

    /**
     * 是否启用
     *
     * @ext 1-启用, 0-禁用; 为空查全部
     */
    private Integer isEnabled;
}
