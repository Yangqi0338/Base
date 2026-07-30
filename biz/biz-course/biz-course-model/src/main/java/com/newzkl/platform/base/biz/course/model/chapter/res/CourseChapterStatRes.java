package com.newzkl.platform.base.biz.course.model.chapter.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 课程章节统计出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CourseChapterStatRes}
 * (源误放 req 包, 此处归位 res)。被"我的已购课程"列表消费,
 * 经 {@code CourseFacade#batchStatChapter} 暴露给 biz-benefit-order。</p>
 *
 * @author KC
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseChapterStatRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 有效章节数量, 启用且未删除
     */
    private Integer chapterCount;

    /**
     * 累计时长, 单位百分秒
     */
    private Long totalDurationCentisecond;

    /**
     * 累计时长, 单位秒, 冗余便于前端使用
     */
    private Double totalDurationSeconds;
}
