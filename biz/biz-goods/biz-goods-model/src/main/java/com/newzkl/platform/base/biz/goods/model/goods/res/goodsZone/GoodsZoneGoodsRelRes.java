package com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分组-商品关联响应VO
 * @author sijiwang
 * @since 2026-03-18
 */
@Data
public class GoodsZoneGoodsRelRes {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 商品ID
     */
    private Long spuId;

    /**
     * 商品名称快照
     */
    private String spu;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}