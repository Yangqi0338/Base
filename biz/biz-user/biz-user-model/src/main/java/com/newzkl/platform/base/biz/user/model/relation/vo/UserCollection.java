package com.newzkl.platform.base.biz.user.model.relation.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户收藏领域实体
 *
 * <p>代表“用户收藏商品”这一核心业务概念，包含业务属性，是领域层核心。</p>
 *
 * @author sijiwang
 */
@Data
@Accessors(chain = true)
public class UserCollection {

    /**
     * 收藏记录唯一标识
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 铺货表ID
     */
    private Long storeDistributionId;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * SPU 名称 (快照)
     */
    private String spuName;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * SKU 名称 (快照)
     */
    private String skuName;

    /**
     * 商品价格 (快照)
     */
    private BigDecimal price;

    /**
     * 商品主图URL (快照)
     */
    private String mainImage;

    /**
     * 已售数量（销量）
     *
     * <p>对齐源 {@code UserCollectionVO.sellNum}，前端 {@code mmt-app/pages/subOrder/collection.vue}
     * 渲染「已售{{item.sellNum}}」。源值来自 {@code IDistributionRpcFacade#queryGoodsSellNum}
     * 实时富化（非收藏快照列）。</p>
     *
     * <p>TODO[infra-gap] 该能力在 biz-market（{@code DistributionDomain#queryGoodsSellNum}），
     * biz-user 无对应出站端口，当前恒为 null。</p>
     */
    private Integer sellNum;

    /**
     * 是否有效：true-有效，false-无效（商品下架等）
     */
    private Boolean isValid;

    /**
     * 是否删除：true-已删除，false-未删除（逻辑删除）
     */
    private Boolean isDeleted;

    /**
     * 收藏时间
     */
    private LocalDateTime collectionTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
