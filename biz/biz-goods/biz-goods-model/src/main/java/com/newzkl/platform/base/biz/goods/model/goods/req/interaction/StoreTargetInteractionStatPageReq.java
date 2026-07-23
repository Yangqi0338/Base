package com.newzkl.platform.base.biz.goods.model.goods.req.interaction;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

/**
 * 门店对象互动统计分页查询入参
 * @author sijiwang
 */
@Data
public class StoreTargetInteractionStatPageReq {
    /**
     * 页码（默认1）
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    /**
     * 页大小（默认20，最大100）
     */
    @Min(value = 1, message = "页大小不能小于1")
    private Integer pageSize = 20;

    /**
     * 门店ID（精确匹配）
     */
    private Long storeId;

    /**
     * 目标类型编码（如GOODS/ACTIVITY，对应TargetTypeEnum的code）
     */
    private String targetTypeCode;

    /**
     * 目标ID列表（批量匹配）
     */
    private List<Long> targetIdList;

    /**
     * 浏览量最小值（用于范围查询）
     */
    private Integer viewCountMin;

    /**
     * 点赞量最小值（用于范围查询）
     */
    private Integer likeCountMin;

    /**
     * 分享量最小值（用于范围查询）
     */
    private Integer shareCountMin;
}