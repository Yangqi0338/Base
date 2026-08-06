package com.newzkl.platform.base.biz.course.model.course.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Size;

/**
 * 课程基础信息新增/编辑入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CourseBaseAddReq}。
 * 价格入参单位为元({@code Double}), 落库转为分({@code Long}), 与旧实现一致。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseReq extends BaseReq {

    /**
     * 课程编码
     *
     * <p>新增时由 {@code BusinessType#COURSE} 自动生成, 编辑时不可改。</p>
     */
    private String courseNum;

    /**
     * 课程标题
     */
    @NotBlank(message = "课程标题不能为空")
    @Size(max = 20, message = "课程标题最多30个字")
    private String title;

    /**
     * 课程简介
     */
    @Size(max = 200, message = "课程简介最多300个字")
    private String intro;

    /**
     * 讲师ID, 关联讲师表主键
     */
    @NotNull(message = "讲师ID不能为空")
    private Long lecturerId;

    /**
     * 课程分类ID, 关联课程分类表主键
     */
    @NotNull(message = "课程分类ID不能为空")
    private Long categoryId;

    /**
     * 原价 (Money; JSON 入参数字/字符串按元反序列化)
     */
    private Money originalPrice;

    /**
     * 售价 (Money; JSON 入参数字/字符串按元反序列化)
     */
    private Money sellPrice;

    /**
     * 虚拟购买次数
     */
    private Integer virtualPurchaseCount = 0;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    @NotNull(message = "启用状态不能为空")
    private Integer isEnabled = 1;
}
