package com.newzkl.platform.base.biz.course.model.chapter.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 课程章节出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.res.CourseChapterRes}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseChapterRes extends BaseRes {

    /**
     * 所属课程ID
     */
    private Long courseId;

    /**
     * 所属课程名称, 冗余展示
     */
    private String courseName;

    /**
     * 章节标题
     */
    private String title;

    /**
     * 章节数
     */
    private Integer chapterNum;

    /**
     * 虚拟学习人数
     */
    private Integer virtualStudyCount;

    /**
     * 是否免费: 1-是, 0-否
     */
    private Integer isFree;

    /**
     * 是否免费描述: 免费/付费
     */
    private String isFreeDesc;

    /**
     * 自媒体上传视频URL
     */
    private String selfMediaUrl;

    /**
     * 外部自媒体链接
     */
    private String externalMediaUrl;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 视频时长, 单位秒(由库中百分秒换算)
     */
    private Double durationCentisecond;

    /**
     * 视频时长描述
     */
    private String durationDesc;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    private Integer isEnabled;

    /**
     * 启用状态描述: 启用/禁用
     */
    private String isEnabledDesc;
}
