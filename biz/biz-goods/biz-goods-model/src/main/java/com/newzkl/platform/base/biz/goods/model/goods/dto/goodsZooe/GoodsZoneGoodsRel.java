package com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商品分组-商品关联领域实体
 * @author sijiwang
 * @since 2026-03-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsZoneGoodsRel {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 商品ID（SPU ID）
     */
    private Long spuId;

    /**
     * 商品名称快照
     */
    private String spu;

    /**
     * 逻辑删除：0-未删 1-已删
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}