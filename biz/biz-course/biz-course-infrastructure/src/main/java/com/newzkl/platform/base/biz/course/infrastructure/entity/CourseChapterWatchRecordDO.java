package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 课程章节观看记录数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class CourseChapterWatchRecordDO extends BaseDO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     * @ext 关联课程表主键
     */
    private Long courseId;

    /**
     * 章节ID
     * @ext 关联课程章节表主键
     */
    private Long courseChapterId;

    /**
     * 课程编码
     * @ext 冗余字段
     */
    private String courseNum;

    /**
     * 章节数
     * @ext 冗余字段
     */
    private Integer chapterNum;

    /**
     * 是否观看过
     */
    private CommonEnum.YesOrNo isWatched;

    /**
     * 累计观看时长
     * @ext 单位百分秒, 预留字段
     */
    private Integer watchDurationCentisecond;

    /**
     * 上次观看位置
     * @ext 单位百分秒, 断点续播预留字段
     */
    private Integer lastWatchPositionCentisecond;

    /**
     * 首次观看时间
     * @ext 即完成时间
     */
    private LocalDateTime watchTime;

    /**
     * 累计观看次数
     */
    private Integer totalWatchTimes;
}
