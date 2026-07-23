package com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分组响应VO
 * @author sijiwang
 * @since 2026-03-18
 */
@Data
public class GoodsZoneRes {

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
}