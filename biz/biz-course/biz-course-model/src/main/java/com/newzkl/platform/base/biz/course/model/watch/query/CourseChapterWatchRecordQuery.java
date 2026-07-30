package com.newzkl.platform.base.biz.course.model.watch.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程章节观看记录分页查询
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CourseChapterWatchRecordPageReq}
 * (源直接继承 mybatis-plus {@code Page}, 新架构统一继承 {@code BizPageQuery})。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseChapterWatchRecordQuery extends BizPageQuery {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 章节ID
     */
    private Long courseChapterId;

    /**
     * 是否观看过: 1-是, 0-否
     */
    private Integer isWatched;
}
