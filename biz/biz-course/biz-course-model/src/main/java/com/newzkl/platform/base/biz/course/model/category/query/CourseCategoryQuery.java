package com.newzkl.platform.base.biz.course.model.category.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程分类分页查询
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CourseCategoryPageReq}。
 * 偏离说明: 源类直接继承 mybatis-plus {@code Page}, 新架构统一继承
 * {@code BizPageQuery}(pageNo/pageSize + idList/createTime 等通用条件)。
 * 源 {@code includeDeleted} 未保留: 逻辑删除由 {@code BaseDO#delFlag} 的
 * {@code @TableLogic} 统一接管, MyBatis-Plus 查询恒过滤已删除数据。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseCategoryQuery extends BizPageQuery {

    /**
     * 分类名称, 模糊查询
     */
    private String categoryName;

    /**
     * 是否启用: 1-启用, 0-禁用; 为空查全部
     */
    private Integer isEnabled;
}
