package com.newzkl.platform.base.biz.goods.model.goods.entity.interaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 互动统计消息DTO（MQ消息传输对象）
 * @author sijiwang
 */
@Data
public class InteractionStatMessageObj implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @NotNull(message = "目标类型不能为空（PRODUCT/VIDEO）")
    private String targetType;

    @NotNull(message = "目标ID不能为空")
    private Long targetId;

    @NotNull(message = "互动类型不能为空（VIEW/LIKE/SHARE）")
    private String actionType;

    private Integer increment = 1;
}