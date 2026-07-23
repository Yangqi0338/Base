package com.newzkl.platform.base.biz.user.model.interaction.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 批量检查用户互动的返回结果。
 *
 * @author sijiwang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchInteractionResult implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 目标ID（如商品ID、视频ID）
     */
    private Long targetId;
    /**
     * 是否已互动（true-是，false-否）
     */
    private boolean interacted;
}
