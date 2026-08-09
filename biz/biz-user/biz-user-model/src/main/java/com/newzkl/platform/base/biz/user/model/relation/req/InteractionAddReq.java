package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 互动操作新增参数视图对象
 *
 * @author sijiwang
 */
@Data
public class InteractionAddReq {

    /**
     * 操作人用户 ID
     */
    private Long userId;

    /**
     * 被操作对象发布者ID
     * @ext 视频/商品的发布者，关联用户表
     */
    @NotNull
    private Long publisherId;

    /**
     * 门店 ID
     */
    private Long storeId;

    /**
     * 被操作对象的类型
     */
    @NotNull
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 被操作对象的 ID
     */
    @NotNull
    private Long targetId;

    /**
     * 操作类型
     */
    @NotNull
    private InteractionEnum.ActionTypeEnum actionType;
}
