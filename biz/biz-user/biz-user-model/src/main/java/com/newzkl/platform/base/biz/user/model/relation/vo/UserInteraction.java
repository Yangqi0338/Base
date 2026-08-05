package com.newzkl.platform.base.biz.user.model.relation.vo;

import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户互动操作领域实体（存储点赞、转发记录）
 *
 * <p>物理删除：取消操作时直接删除记录。</p>
 *
 * @author sijiwang
 */
@Data
public class UserInteraction {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 操作人ID（关联用户表）
     */
    private Long userId;

    /**
     * 被操作对象发布者ID（视频/商品的发布者，关联用户表）
     */
    private Long publisherId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 被操作对象类型
     */
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 被操作对象ID（视频ID或商品ID，与targetType对应）
     */
    private Long targetId;

    /**
     * 操作类型（LIKE：点赞；SHARE：转发）
     */
    private InteractionEnum.ActionTypeEnum actionType;

    /**
     * 操作创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 操作更新时间
     */
    private LocalDateTime updatedTime;
}
