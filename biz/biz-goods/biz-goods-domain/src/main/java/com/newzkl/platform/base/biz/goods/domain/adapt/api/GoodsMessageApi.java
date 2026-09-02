package com.newzkl.platform.base.biz.goods.domain.adapt.api;

import java.util.List;

/**
 * 商品消息出站端口
 *
 * <p>对齐 biz-order {@code LocalMessageApi}: goods 域写主数据后经本端口发开发者通知事件,
 * infra 实现直接经 {@code MQUtil} 投递(本地消息表 + MQ)。goods 域不依赖 market 域,
 * 无法解析订阅渠道, 故只发 spuId + 事件内容, 收件人解析由 openapi 消费方完成。</p>
 *
 * @author KC
 */
public interface GoodsMessageApi {

    /**
     * 发 SPU 基础信息变更通知
     *
     * @param spuId SPU 主键
     */
    void notifySpuEdit(Long spuId);

    /**
     * 发 SKU 规格变更通知
     *
     * @param spuId SPU 主键
     * @param skuIdList 变更的 SKU 主键列表
     */
    void notifySkuEdit(Long spuId, List<Long> skuIdList);

    /**
     * 发 SKU 规格删除通知
     *
     * @param spuId SPU 主键
     * @param skuIdList 删除的 SKU 主键列表
     */
    void notifySkuDelete(Long spuId, List<Long> skuIdList);

    /**
     * 发 SPU 上下架通知
     *
     * @param spuIdList SPU 主键列表
     * @param sourceState 原销售状态
     * @param newState 新销售状态
     */
    void notifySaleState(List<Long> spuIdList, Integer sourceState, Integer newState);

    /**
     * 发 SKU 价格变更通知
     *
     * @param spuId SPU 主键
     * @param skuIdList 改价的 SKU 主键列表
     */
    void notifySkuPrice(Long spuId, List<Long> skuIdList);
}
