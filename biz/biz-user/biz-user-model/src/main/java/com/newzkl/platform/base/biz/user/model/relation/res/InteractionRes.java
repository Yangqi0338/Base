package com.newzkl.platform.base.biz.user.model.relation.res;

import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 互动操作查询结果视图对象
 *
 * @author sijiwang
 */
@Data
public class InteractionRes extends BaseRes {

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
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 被操作对象的 ID
     */
    private Long targetId;

    /**
     * 操作类型
     */
    private InteractionEnum.ActionTypeEnum actionType;
}
