package com.newzkl.platform.base.biz.course.model.event;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.io.Serializable;

/**
 * 课程章节观看记录新增/更新请求
 * @author sijiwang
 */
@Data
public class CourseChapterWatchRecordAddReq implements Serializable {

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 课程ID
     */
    @NotNull
    private Long courseId;

    /**
     * 章节ID（必填）
     */
    @NotNull
    private Long courseChapterId;

    /**
     * 课程编码
     */
    private String courseNo;

    /**
     * 章节数
     */
    private Integer chapterNum;
}