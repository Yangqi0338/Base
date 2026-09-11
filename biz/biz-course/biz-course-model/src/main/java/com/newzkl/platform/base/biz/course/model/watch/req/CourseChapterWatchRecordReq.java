package com.newzkl.platform.base.biz.course.model.watch.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程章节观看记录新增/更新入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.rpc.model.course.CourseChapterWatchRecordAddReq}
 * (源居 rpc 模块, 按迁移约定 VO 搬进 model)。</p>
 *
 * <p>{@code userId} 在 HTTP 入口由 {@code UserAccountApi#currentUserId()} 覆写为当前登录用户,
 * 旧实现走 {@code SecurityUtils.getAccountId()}; MQ 异步通道则由消息体直接携带。</p>
 *
 * @author KC
 */
@Data
public class CourseChapterWatchRecordReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 章节ID
     */
    @NotNull(message = "章节ID不能为空")
    private Long courseChapterId;

    /**
     * 课程编码, 冗余字段
     */
    private String courseNo;

    /**
     * 章节数, 冗余字段
     */
    private Integer chapterNum;
}
