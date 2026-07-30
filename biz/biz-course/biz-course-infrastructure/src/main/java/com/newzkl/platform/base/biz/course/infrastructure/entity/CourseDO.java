package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程数据对象
 *
 * <p>迁移自 {@code com.zkl.scm.user.infrastructure.entity.CourseDO}(表 {@code course})。
 * 偏离说明: 源逻辑删除列 {@code is_deleted} 由 {@link BaseDO#getDelFlag} 承担,
 * 源 {@code createBy}/{@code updateBy} 由 {@link BaseDO#getExecutor} 承担。</p>
 *
 * <p>价格列单位为分, 入参单位为元, 换算在仓储实现完成。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("course")
public class CourseDO extends BaseDO {

    /**
     * 课程编码, K 前缀自编码
     */
    private String courseNum;

    /**
     * 课程标题
     */
    private String title;

    /**
     * 课程简介
     */
    private String intro;

    /**
     * 讲师ID, 关联讲师表主键
     */
    private Long lecturerId;

    /**
     * 课程分类ID, 关联课程分类表主键
     */
    private Long categoryId;

    /**
     * 原价, 单位分
     */
    private Long originalPrice;

    /**
     * 售价, 单位分
     */
    private Long sellPrice;

    /**
     * 虚拟购买次数
     */
    private Integer virtualPurchaseCount;

    /**
     * 实际购买数量
     */
    private Integer purchaseCount;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 轮播图URL, 多个用逗号分隔
     */
    private String carouselImages;

    /**
     * 视频介绍URL
     */
    private String videoUrl;

    /**
     * 课程详情富文本
     */
    private String details;

    /**
     * 章节总数, 冗余字段
     */
    private Integer chapterCount;

    /**
     * 课程总时长, 单位百分秒, 冗余字段
     */
    private Long totalDurationCentisecond;

    /**
     * 课程总时长描述, 冗余字段
     */
    private String totalDurationDesc;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    private Integer isEnabled;
}
