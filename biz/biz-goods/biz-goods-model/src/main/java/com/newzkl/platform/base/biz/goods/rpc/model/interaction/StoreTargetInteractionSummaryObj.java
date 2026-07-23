package com.newzkl.platform.base.biz.goods.rpc.model.interaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 门店目标互动统计汇总（按target_type+target_id分组）
 * @author sijiwang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreTargetInteractionSummaryObj implements Serializable {
    /** 门店ID */
    private Long storeId;
    /** 创建者ID */
    private Long publisherId;
    /** 目标类型（如PRODUCT/ARTICLE） */
    private String targetType;
    /** 目标ID（如商品ID/文章ID） */
    private Long targetId;
    /** 累计浏览量 */
    private Integer totalViewCount;
    /** 累计点赞量 */
    private Integer totalLikeCount;
    /** 累计分享量 */
    private Integer totalShareCount;
}