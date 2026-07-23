package com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商品分组领域实体（DDD）
 * @author sijiwang
 * @since 2026-03-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsZone {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组描述
     */
    private String groupDesc;

    /**
     * 背景图
     */
    private String backgroundImg;

    /**
     * 排序类型：1-默认 2-销量从高到低 3-上架时间倒序
     */
    private Integer sortType;

    /**
     * 搜索框显示状态：0-不显示 1-显示
     */
    private Integer searchBoxStatus;

    /**
     * 价格显示状态：0-不显示 1-显示
     */
    private Integer priceShowStatus;

    /**
     * 门店显示状态：0-不显示 1-显示
     */
    private Integer storeShowStatus;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 分组状态：0-禁用 1-启用
     */
    private Integer state;

    /**
     * 创建人ID
     */
    private Long createId;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // === 领域行为 ===
    /**
     * 启用分组
     */
    public void enable() {
        this.state = 1;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 禁用分组
     */
    public void disable() {
        this.state = 0;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 更新商品数量
     * @param goodsNum 新商品数量
     */
    public void updateGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
        this.updateTime = LocalDateTime.now();
    }
}