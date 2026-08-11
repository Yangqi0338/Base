package com.newzkl.platform.base.biz.user.model.interaction.query;

import com.newzkl.platform.base.biz.user.model.interaction.req.InteractionBatchItem;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量检查用户互动的查询参数
 *
 * @author sijiwang
 */
@Data
public class BatchInteractionQuery implements Serializable {
    /**
     * 目标类型
     */
    @NotNull
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 操作类型
     */
    @NotNull
    private InteractionEnum.ActionTypeEnum actionType;

    /**
     * 批量检查的子项列表
     */
    @NotEmpty
    private List<InteractionBatchItem> items;
}
