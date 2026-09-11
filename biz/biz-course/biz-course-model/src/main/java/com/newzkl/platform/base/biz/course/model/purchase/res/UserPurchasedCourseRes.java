package com.newzkl.platform.base.biz.course.model.purchase.res;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.newzkl.platform.base.biz.course.model.course.vo.CourseExpandVO;
import com.newzkl.platform.base.common.ddd.model.enums.course.CourseEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户已购课程列表出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.res.UserPurchasedCourseRes}。
 * 源经 XML LEFT JOIN course 联表填充课程字段; 本域课程与购买同域, 改由领域层
 * 逐记录调 {@code CourseDomain#getById} + 观看统计装配, 不走联表 SQL。</p>
 *
 * @author KC
 */
@Data
public class UserPurchasedCourseRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 购买记录ID
     */
    private Long purchaseRecordId;

    /**
     * 支付状态: 0-待支付, 1-支付成功, 2-支付失败
     */
    private CourseEnum.PurchasePayStateEnum payState;

    /**
     * 实际支付金额(分)
     */
    private Long payPrice;

    /**
     * 原价(分)
     */
    private Long originalPrice;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付方式: 1-微信支付, 2-支付宝支付
     */
    private Integer payType;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 讲师ID
     */
    private Long lecturerId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程编码
     */
    private String courseNo;

    /**
     * 课程标题
     */
    private String courseTitle;

    /**
     * 课程分类ID
     */
    private Long categoryId;

    /**
     * 课程封面图URL
     */
    private String coverImage;

    /**
     * 已观看章节的数量
     */
    private Integer watchCount;

    /**
     * 累计时长(百分秒)
     */
    private Long totalDurationCentisecond;

    /**
     * 累计时长(秒, 冗余便于前端使用)
     */
    private Double totalDurationSeconds;

    /**
     * 章节总数
     */
    private Integer chapterCount;

    /**
     * 课程简介
     */
    private String intro;

    @JsonUnwrapped
    public CourseExpandVO expand;
}
