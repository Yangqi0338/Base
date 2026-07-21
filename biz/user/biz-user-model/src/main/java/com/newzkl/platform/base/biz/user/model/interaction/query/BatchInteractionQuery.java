package com.newzkl.platform.base.biz.user.model.interaction.query;

import com.newzkl.platform.base.biz.user.model.enums.InteractionEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量检查用户互动的查询参数。
 *
 * @author sijiwang
 */
@Data
public class BatchInteractionQuery implements Serializable {
    /**
     * 目标类型（批量查询的所有子项共用该类型）
     */
    @NotNull(message = "目标类型不能为空")
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 操作类型（批量查询的所有子项共用该类型）
     */
    @NotNull(message = "操作类型不能为空")
    private InteractionEnum.ActionTypeEnum actionType;

    /**
     * 批量检查的子项列表
     */
    @NotEmpty(message = "批量检查的子项列表不能为空")
    private List<InteractionBatchItem> items;
}
