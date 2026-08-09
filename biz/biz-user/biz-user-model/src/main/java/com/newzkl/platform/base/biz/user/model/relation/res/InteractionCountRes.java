package com.newzkl.platform.base.biz.user.model.relation.res;

import lombok.Data;

/**
 * 互动操作统计结果视图对象
 *
 * @author sijiwang
 */
@Data
public class InteractionCountRes {

    /**
     * 被统计对象的 ID
     */
    private Long targetId;

    /**
     * 统计的操作类型
     */
    private String actionType;

    /**
     * 操作的总数量
     */
    private Integer count;
}
