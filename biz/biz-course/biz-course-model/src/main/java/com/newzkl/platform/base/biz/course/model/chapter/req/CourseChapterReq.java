package com.newzkl.platform.base.biz.course.model.chapter.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 课程章节新增/编辑入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CourseChapterAddReq}。
 * {@code courseId} 指向 {@code course.id}(源字段注释写"课程编号"但类型与用法均为课程主键)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseChapterReq extends BaseReq {

    /**
     * 所属课程ID, 关联课程表主键
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 章节标题
     */
    @NotBlank(message = "章节标题不能为空")
    @Size(max = 10, message = "章节标题最多10个字")
    private String title;

    /**
     * 章节数, 从1开始, 同一课程下不重复
     */
    @NotNull(message = "章节数不能为空")
    @Min(value = 1, message = "章节数必须大于等于1")
    private Integer chapterNum;

    /**
     * 虚拟学习人数
     */
    @Min(value = 1, message = "虚拟学习人数必须大于等于1")
    private Integer virtualStudyCount = 1;

    /**
     * 是否免费: 1-是, 0-否
     */
    @NotNull(message = "是否免费不能为空")
    private Integer isFree = 1;

    /**
     * 视频时长, 单位秒(保留2位小数), 落库转百分秒整数
     */
    @NotNull(message = "时长")
    private Double durationCentisecond;

    /**
     * 自媒体上传视频URL, 与外部链接二选一
     */
    private String selfMediaUrl;

    /**
     * 外部自媒体链接, 与自媒体URL二选一
     */
    private String externalMediaUrl;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    @NotNull(message = "启用状态不能为空")
    private Integer isEnabled = 1;

    /**
     * 发布时间, 为空取当前时间
     */
    private LocalDateTime publishTime;
}
