package com.newzkl.platform.base.biz.course.model.course.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程分页查询
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CoursePageReq}
 * (源直接继承 mybatis-plus {@code Page}, 新架构统一继承 {@code BizPageQuery})。
 * 管理端 {@code /course/page} 与 C 端 {@code /appCourse/page} 共用本查询对象, 与旧实现一致。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseQuery extends BizPageQuery {

    /**
     * 课程标题, 模糊查询
     */
    private String title;

    /**
     * 课程分类ID
     */
    private Long categoryId;

    /**
     * 讲师ID
     */
    private Long lecturerId;

    /**
     * 是否启用
     *
     * @ext 1-启用, 0-禁用; 为空查全部
     */
    private Integer isEnabled;
}
