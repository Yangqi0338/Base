package com.newzkl.platform.base.biz.user.model.relation.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 互动操作新增参数视图对象
 *
 * @author sijiwang
 */
@Data
public class InteractionAddVO {

    /**
     * 操作人用户 ID
     */
    private Long userId;

    /**
     * 被操作对象发布者ID（视频/商品的发布者，关联用户表）
     */
    @NotNull(message = "视频或者商品的发布者id不能为空")
    private Long publisherId;

    /**
     * 门店 ID
     */
    private Long storeId;

    /**
     * 被操作对象的类型
     */
    @NotBlank(message = "对象类型不能为空")
    private String targetType;

    /**
     * 被操作对象的 ID
     */
    @NotNull(message = "对象 ID 不能为空")
    private Long targetId;

    /**
     * 操作类型
     */
    @NotBlank(message = "操作类型不能为空")
    private String actionType;
}
