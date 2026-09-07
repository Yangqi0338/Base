package com.newzkl.platform.base.biz.goods.domain.adapt.api;

import java.util.List;

/**
 * 商品业务事件出站端口
 *
 * <p>对齐 biz-order {@code LocalMessageApi}: goods 域写完主数据后经本端口发布商品业务事件,
 * infra 实现经 {@code MQUtil} 投到各自 tag。本端口只描述「商品发生了什么」, 不含收件人概念,
 * 也不知道有哪些订阅方 —— 订阅方自行反查收件人并决定推送形态</p>
 *
 * @author KC
 */
public interface EventApi {

    /**
     * 发布 SPU 基础信息变更事件
     *
     * @param spuId SPU 主键
     */
    void publishSpuEdit(Long spuId);

    /**
     * 发布 SKU 规格变更事件
     *
     * @param spuId SPU 主键
     * @param skuIdList 变更的 SKU 主键列表
     */
    void publishSkuEdit(Long spuId, List<Long> skuIdList);

    /**
     * 发布 SKU 规格删除事件
     *
     * @param spuId SPU 主键
     * @param skuIdList 删除的 SKU 主键列表
     */
    void publishSkuDelete(Long spuId, List<Long> skuIdList);

    /**
     * 发布 SPU 上下架事件
     *
     * <p>按 spuId 逐条投递 每条消息只带自己那一个 spuId</p>
     *
     * @param spuIdList SPU 主键列表
     * @param sourceState 原销售状态 未知传 null
     * @param newState 新销售状态
     */
    void publishSaleState(List<Long> spuIdList, Integer sourceState, Integer newState);

    /**
     * 发布 SKU 价格变更事件
     *
     * @param spuId SPU 主键
     * @param skuIdList 改价的 SKU 主键列表
     */
    void publishSkuPrice(Long spuId, List<Long> skuIdList);
}
