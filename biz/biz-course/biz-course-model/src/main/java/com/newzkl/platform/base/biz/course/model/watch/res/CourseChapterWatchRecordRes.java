package com.newzkl.platform.base.biz.course.model.watch.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 课程章节观看记录出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.res.CourseChapterWatchRecordRes}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseChapterWatchRecordRes extends BaseRes {

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
     * 课程编码, 冗余字段
     */
    private String courseNum;

    /**
     * 章节数, 冗余字段
     */
    private Integer chapterNum;

    /**
     * 是否观看过: 1-是, 0-否
     */
    private Integer isWatched;

    /**
     * 观看状态描述: 已观看/未观看
     */
    private String isWatchedDesc;

    /**
     * 累计观看次数
     */
    private Integer totalWatchTimes;

    /**
     * 首次观看时间, 即完成时间
     */
    private LocalDateTime watchTime;
}
