package com.newzkl.platform.base.biz.course.model.course.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程 C 端列表出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.res.AppCourseRes}, 供
 * {@code /appCourse/page} 使用。相对 {@code CourseRes} 多出 {@code watchCount}
 * (当前用户已观看章节数), 其余字段一致, 故直接继承 {@code CourseRes} 复用。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppCourseRes extends CourseRes {

    /**
     * 当前用户已观看章节的数量
     */
    private Integer watchCount;
}
