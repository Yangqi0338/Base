package com.newzkl.platform.base.biz.user.model.relation.dto;

import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import lombok.Data;

/**
 * 用户互动操作领域实体（存储点赞、转发记录）
 *
 * <p>物理删除：取消操作时直接删除记录。</p>
 *
 * @author sijiwang
 */
@Data
public class UserInteractionDTO extends BaseDTO {

    /**
     * 操作人ID
     */
    private Long userId;

    /**
     * 被操作对象发布者ID
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
     * 被操作对象ID
     */
    private Long targetId;

    /**
     * 操作类型
     */
    private InteractionEnum.ActionTypeEnum actionType;
}
