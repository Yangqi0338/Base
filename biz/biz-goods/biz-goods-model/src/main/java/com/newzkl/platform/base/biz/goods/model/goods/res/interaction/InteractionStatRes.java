package com.newzkl.platform.base.biz.goods.model.goods.res.interaction;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 互动统计响应VO（前端展示用）
 * @author sijiwang
 */
@Data
public class InteractionStatRes {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 目标id
     */
    private Long targetId;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 转发量
     */
    private Integer shareCount;

    /**
     * 最后更新时间
     */
    private LocalDateTime updatedTime;

    private Map<String, Object> extMap;

}