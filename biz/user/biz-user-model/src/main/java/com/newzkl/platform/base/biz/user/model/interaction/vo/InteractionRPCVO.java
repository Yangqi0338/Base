package com.newzkl.platform.base.biz.user.model.interaction.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 互动操作查询结果 RPC 视图对象。
 *
 * @author sijiwang
 */
@Data
public class InteractionRPCVO implements Serializable {

    /**
     * 互动记录的主键 ID
     */
    private Long id;

    /**
     * 操作人用户 ID
     */
    private Long userId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 被操作对象的类型
     */
    private String targetType;

    /**
     * 被操作对象的 ID
     */
    private Long targetId;

    /**
     * 操作类型
     */
    private String actionType;

    /**
     * 操作类型数量 点赞数量 转发数量 和操作类型对应
     */
    private Integer actionTypeCount;

    /**
     * 操作的创建时间
     */
    private LocalDateTime createdTime;
}
