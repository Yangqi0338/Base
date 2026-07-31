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

    /** 门店ID */
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    /** 目标类型 */
    @NotNull(message = "目标类型不能为空")
    private String targetType;

    /** 目标ID */
    @NotNull(message = "目标ID不能为空")
    private Long targetId;

    /** 浏览数 */
    private Integer viewCount = 0;

    /** 点赞数 */
    private Integer likeCount = 0;

    /** 分享数 */
    private Integer shareCount = 0;

    /** 扩展字段 */
    private Map<String, Object> extMap;
}