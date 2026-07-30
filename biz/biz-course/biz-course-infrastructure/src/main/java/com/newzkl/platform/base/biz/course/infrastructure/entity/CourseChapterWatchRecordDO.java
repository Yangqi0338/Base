package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 课程章节观看记录数据对象
 *
 * <p>迁移自 {@code com.zkl.scm.user.infrastructure.entity.CourseChapterWatchRecordDO}
 * (表 {@code course_chapter_watch_record})。偏离说明: 源逻辑删除列 {@code is_deleted}
 * 由 {@link BaseDO#getDelFlag} 承担, 源 {@code createBy}/{@code updateBy}
 * 由 {@link BaseDO#getExecutor} 承担。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("course_chapter_watch_record")
public class CourseChapterWatchRecordDO extends BaseDO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID, 关联课程表主键
     */
    private Long courseId;

    /**
     * 章节ID, 关联课程章节表主键
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
     * 累计观看时长, 单位百分秒, 预留字段
     */
    private Integer watchDurationCentisecond;

    /**
     * 上次观看位置, 单位百分秒, 断点续播预留字段
     */
    private Integer lastWatchPositionCentisecond;

    /**
     * 首次观看时间, 即完成时间
     */
    private LocalDateTime watchTime;

    /**
     * 累计观看次数
     */
    private Integer totalWatchTimes;
}
