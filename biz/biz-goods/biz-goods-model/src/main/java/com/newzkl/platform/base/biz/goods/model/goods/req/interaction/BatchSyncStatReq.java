package com.newzkl.platform.base.biz.goods.model.goods.req.interaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 批量同步统计数据入参 VO
 * @author sijiwang
 */
@Data
public class BatchSyncStatReq {

    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @NotNull(message = "目标类型不能为空")
    private String targetType;

    @NotNull(message = "目标ID不能为空")
    private Long targetId;

    private Integer viewCount = 0;

    private Integer likeCount = 0;

    private Integer shareCount = 0;

    private Map<String, Object> extMap;
}