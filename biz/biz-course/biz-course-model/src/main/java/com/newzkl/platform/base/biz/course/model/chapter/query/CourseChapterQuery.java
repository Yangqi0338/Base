package com.newzkl.platform.base.biz.course.model.chapter.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程章节分页查询
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CourseChapterPageReq}
 * (源直接继承 mybatis-plus {@code Page}, 新架构统一继承 {@code BizPageQuery})。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseChapterQuery extends BizPageQuery {

    /**
     * 所属课程ID, 查某课程下的章节
     */
    private Long courseId;

    /**
     * 章节标题, 模糊查询
     */
    private String title;

    /**
     * 是否免费: 1-免费, 0-付费; 为空查全部
     */
    private Integer isFree;

    /**
     * 是否启用: 1-启用, 0-禁用; 为空查全部
     */
    private Integer isEnabled;
}
