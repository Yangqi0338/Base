package com.newzkl.platform.base.biz.course.model.watch.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 课程观看章节数统计出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.res.CourseWatchStatisticRes}。
 * 供 C 端课程列表回填"已观看章节数"。</p>
 *
 * @author KC
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseWatchStatisticRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 已观看章节数量
     */
    private Integer watchedChapterCount;
}
