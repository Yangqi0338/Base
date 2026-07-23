package com.newzkl.platform.base.biz.store.domain.adapt.api;

import java.util.List;
import java.util.Map;

/**
 * 分销域跨服务出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.market.rpc.facade.IDistributionRpcFacade}。</p>
 *
 * @author KC
 */
public interface DistributionApi {

    /**
     * 按门店ID列表随机查询分销商品。
     *
     * @param storeIdList 门店ID列表
     * @param limit       每店随机数量
     * @return 门店ID -> 随机分销商品列表, 无则空 map
     */
    Map<Long, List<DistributionRandomInfo>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, int limit);

    /**
     * 查询门店累计销量。
     *
     * @param storeId 门店ID
     * @return 累计销量, 无则 0
     */
    Integer getStoreTotalSellNum(Long storeId);

    /**
     * 批量复制分销商品到目标账户。
     *
     * @param sourceChannelId 源渠道ID
     * @param targetAccountId 目标账户ID
     * @param goodsIdList     商品ID集合
     */
    void batchCopyDistribution(Long sourceChannelId, Long targetAccountId, java.util.Set<Long> goodsIdList);
}
