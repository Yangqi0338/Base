package com.newzkl.platform.base.biz.course.model.lecturercategory.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Size;

/**
 * 讲师分类新增/编辑入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.req.LecturerCategoryAddReq}。
 * 偏离说明: 源 {@code operator} 字段由 controller 硬编码 {@code "system"} 填充,
 * 新架构下操作人由 {@code BaseDO#executor} 自动填充, 故不保留。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerCategoryReq extends BaseReq {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 10, message = "分类名称不能超过10个字")
    private String categoryName;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 是否启用
     *
     * @ext 1-启用, 0-禁用
     */
    @NotNull(message = "启用状态不能为空")
    private Integer isEnabled;
}
