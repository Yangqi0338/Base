package com.newzkl.platform.base.biz.course.model.category.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Size;

/**
 * 课程分类新增/编辑入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.req.CourseCategoryAddReq}。
 * 偏离说明: 源 {@code operator} 字段由 controller 硬编码 {@code "system"} 填充,
 * 新架构下操作人由 {@code BaseDO#executor} 自动填充, 故不保留该字段。
 * {@code id} 继承自 {@code BaseReq}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseCategoryReq extends BaseReq {

    /**
     * 分类编码
     *
     * <p>新增时由 {@code BusinessType#COURSE_CATEGORY} 自动生成, 编辑时不可改。</p>
     */
    private String categoryCode;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 6, message = "分类名称最多6个字")
    private String categoryName;

    /**
     * 小标题
     */
    @Size(max = 15, message = "小标题最多15个字")
    private String subTitle;

    /**
     * 排序值, 值越小越靠前
     */
    private Integer sort = 0;

    /**
     * 是否启用: 1-启用, 0-禁用
     */
    @NotNull(message = "启用状态不能为空")
    private Integer isEnabled = 1;
}
