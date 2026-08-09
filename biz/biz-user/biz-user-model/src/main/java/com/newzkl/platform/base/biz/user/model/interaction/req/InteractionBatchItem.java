package com.newzkl.platform.base.biz.user.model.interaction.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 批量互动检查的子项参数
 *
 * @author sijiwang
 */
@Data
public class InteractionBatchItem implements Serializable {
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 目标ID（如商品ID、视频ID）
     */
    @NotNull(message = "目标ID不能为空")
    private Long targetId;

    /**
     * 构造方法
     *
     * @param targetId 目标ID
     * @param userId   用户ID
     */
    public InteractionBatchItem(Long targetId, Long userId) {
        this.targetId = targetId;
        this.userId = userId;
    }

}
