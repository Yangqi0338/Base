package com.newzkl.platform.base.biz.goods.model.goods.req.interaction;


import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import lombok.Data;

import java.util.List;

/**
 * 门店对象互动统计批量查询入参
 * @author sijiwang
 */
@Data
public class StoreTargetInteractionStatBatchReq {
    /**
     * 门店ID列表（批量匹配）
     */
    private List<Long> storeIdList;

    /**
     * 目标类型（枚举，精确匹配）
     */
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 目标ID列表（批量匹配）
     */
    private List<Long> targetIdList;

    /**
     * 浏览量范围 [min, max]（数组长度2，null表示不限制）
     */
    private Integer[] viewCountRange;

    /**
     * 点赞量范围 [min, max]（数组长度2，null表示不限制）
     */
    private Integer[] likeCountRange;

    /**
     * 分享量范围 [min, max]（数组长度2，null表示不限制）
     */
    private Integer[] shareCountRange;
}