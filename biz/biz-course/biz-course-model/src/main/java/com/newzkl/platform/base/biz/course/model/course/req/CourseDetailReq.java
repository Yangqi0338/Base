package com.newzkl.platform.base.biz.course.model.course.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Size;

/**
 * 课程详情编辑入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CourseDetailUpdateReq}。
 * 与 {@code CourseReq} 分离: 基础信息与富文本详情走两个独立端点
 * ({@code /course/editBase} 与 {@code /course/editDetail}), 沿用旧契约。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseDetailReq extends BaseReq {

    /**
     * 封面图URL
     */
    @NotBlank(message = "封面图不能为空")
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
    @Size(max = 200, message = "课程详情最多500个字")
    private String details;
}
