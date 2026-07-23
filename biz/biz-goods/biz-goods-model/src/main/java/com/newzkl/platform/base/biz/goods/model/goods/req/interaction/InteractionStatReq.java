package com.newzkl.platform.base.biz.goods.model.goods.req.interaction;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 互动统计请求VO（前端调用接口入参）
 * @author sijiwang
 */
@Data
public class InteractionStatReq {

    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @NotEmpty(message = "目标类型不能为空（PRODUCT/VIDEO）")
    private String targetType;

    @NotNull(message = "目标ID不能为空")
    private Long targetId;

    @NotEmpty(message = "互动类型不能为空（VIEW/LIKE/SHARE）")
    private String actionType;

    /**
     * 是否取消操作（true=取消，false=新增，默认false）
     * 示例：取消点赞→isCancel=true
     */
    private Boolean isCancel = false;
}