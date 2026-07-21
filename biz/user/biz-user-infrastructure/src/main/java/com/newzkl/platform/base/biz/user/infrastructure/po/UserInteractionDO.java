package com.newzkl.platform.base.biz.user.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.user.model.enums.InteractionEnum;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 用户互动操作持久化对象（存储点赞、转发记录）。
 *
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class UserInteractionDO extends BaseDO {

    /**
     * 操作人ID（关联用户表）
     */
    @Index
    private Long userId;

    /**
     * 被操作对象发布者ID（视频/商品的发布者，关联用户表）
     */
    @Index
    private Long publisherId;

    /**
     * 门店ID
     */
    @Index
    private Long storeId;

    /**
     * 被操作对象类型
     */
    @Index
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 被操作对象ID（视频ID或商品ID，与targetType对应）
     */
    @Index
    private Long targetId;

    /**
     * 操作类型（LIKE：点赞；SHARE：转发）
     */
    private InteractionEnum.ActionTypeEnum actionType;
}
