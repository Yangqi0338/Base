package com.newzkl.platform.base.biz.course.model.lecturer.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Size;

/**
 * 讲师新增/编辑入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.req.LecturerAddReq}。
 * {@code mainAccountId} 为存储型外键(讲师绑定的主体账号), 非运行时跨域调用。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerReq extends BaseReq {

    /**
     * 讲师名称, 即渠道商名称
     */
    @Size(max = 64, message = "讲师名称不能超过64个字")
    private String lecturerName;

    /**
     * 主体账号ID, 关联主体账号表主键
     */
    @NotNull(message = "主体账号ID不能为空")
    private Long mainAccountId;

    /**
     * 讲师分类ID, 关联讲师分类表主键
     */
    @NotNull(message = "讲师分类不能为空")
    private Long lecturerCategoryId;

    /**
     * 个人简介
     */
    @Size(max = 200, message = "个人简介不能超过200个字")
    private String personalIntro;

    /**
     * 封面图URL
     */
    private String coverImageUrl;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 是否启用
     *
     * @ext 1-启用, 0-禁用
     */
    @NotNull(message = "启用状态不能为空")
    private Integer isEnabled;
}
