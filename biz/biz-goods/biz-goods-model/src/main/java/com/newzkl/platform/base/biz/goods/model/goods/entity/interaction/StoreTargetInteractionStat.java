package com.newzkl.platform.base.biz.goods.model.goods.entity.interaction;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 门店-对象互动统计 领域模型（DDD 领域层核心，仓储接口操作此对象）
 * @author sijiwang
 */
@Data
public class StoreTargetInteractionStat {

    /** 领域模型唯一标识（对应 DO 的 id） */
    private Long id;

    /** 门店ID（关联门店表） */
    private Long storeId;

    /**
     * 被操作对象发布者ID（视频/商品的发布者，关联用户表）
     */
    private Long publisherId;

    /** 被统计对象类型（领域枚举） */
    private String targetType;

    /** 被统计对象ID（商品/视频ID） */
    private Long targetId;

    /** 浏览量 */
    private Integer viewCount ;

    /** 点赞量 */
    private Integer likeCount ;

    /** 转发量 */
    private Integer shareCount ;

    /** 创建时间 */
    private LocalDateTime createdTime;

    /** 更新时间 */
    private LocalDateTime updatedTime;

    /** 扩展字段（领域层用 Map 存储，DO 用 JSON 字符串存储） */
    private Map<String, Object> extMap;
}