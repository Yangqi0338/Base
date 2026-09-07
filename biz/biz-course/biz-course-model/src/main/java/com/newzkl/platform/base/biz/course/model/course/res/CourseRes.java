package com.newzkl.platform.base.biz.course.model.course.res;

import com.newzkl.platform.base.common.core.model.annotation.JsonTranslate;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.res.CourseRes}。
 * 价格出参单位为元({@code Double}), 由库中分值换算, 与旧实现一致。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseRes extends BaseRes {

    /**
     * 课程编码
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
     * 讲师ID
     */
    private Long lecturerId;

    /**
     * 讲师名称, 冗余展示
     */
    private String lecturerName;

    /**
     * 课程分类ID
     */
    private Long categoryId;

    /**
     * 分类名称, 冗余展示
     */
    private String categoryName;

    /**
     * 原价 (Money; JSON 出参按元字符串序列化)
     */
    private Money originalPrice;

    /**
     * 售价 (Money; JSON 出参按元字符串序列化)
     */
    private Money sellPrice;

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

    /**
     * 是否付费: 1-付费, 0-未付费
     */
    private Integer isPay = 0;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    @JsonTranslate(index = 1)
    private CommonEnum.YesOrNo isEnabled;
}
