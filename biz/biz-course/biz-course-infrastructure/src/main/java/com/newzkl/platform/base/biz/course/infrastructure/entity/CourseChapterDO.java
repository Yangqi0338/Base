package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 课程章节数据对象
 *
 * <p>迁移自 {@code com.zkl.scm.user.infrastructure.entity.CourseChapterDO}(表 {@code course_chapter})。
 * 偏离说明: 源逻辑删除列 {@code is_deleted} 由 {@link BaseDO#getDelFlag} 承担,
 * 源 {@code createBy}/{@code updateBy} 由 {@link BaseDO#getExecutor} 承担。</p>
 *
 * <p>{@code courseId} 指向 {@code course.id}(源字段注释写"课程编号", 实际类型与用法均为课程主键)。
 * 时长列单位为百分秒, 入参单位为秒, 换算在仓储实现完成。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class CourseChapterDO extends BaseDO {

    /**
     * 所属课程ID
     * @ext 关联课程表主键
     */
    private Long courseId;

    /**
     * 章节标题
     */
    private String title;

    /**
     * 章节数
     * @ext 从1开始, 同一课程下不重复
     */
    private Integer chapterNum;

    /**
     * 虚拟学习人数
     */
    private Integer virtualStudyCount;

    /**
     * 是否免费
     */
    private CommonEnum.YesOrNo isFree;

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
     * 章节视频时长
     * @ext 单位百分秒
     */
    private Integer durationCentisecond;

    /**
     * 时长描述
     * @ext 冗余字段用于前端展示
     */
    private String durationDesc;

    /**
     * 是否启用
     */
    private CommonEnum.YesOrNo isEnabled;
}
