package com.newzkl.platform.base.biz.order.model.support.api.interaction;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店-对象互动统计事件（MQ 消息传输模型，参考 GoodsPaySuccessEvent 风格）
 * @author sijiwang
 */
@Data
public class StoreTargetInteractionEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 被操作对象发布者ID（视频/商品的发布者，关联用户表）
     */
    private Long publisherId;

    /** 目标类型（PRODUCT/VIDEO） */
    private String targetType;

    /** 目标ID（商品ID/视频ID） */
    private Long targetId;

    /** 互动类型（VIEW/LIKE/SHARE） */
    private String actionType;

    /** 增量（默认+1） */
    private Integer increment;
}