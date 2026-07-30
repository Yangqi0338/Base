package com.newzkl.platform.base.biz.account.domain.adapt.api;

import java.util.List;
import java.util.Map;

/**
 * 营销域铺货出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.market.rpc.facade.IDistributionRpcFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface MarketDistributionApi {

    /**
     * 按门店随机取铺货商品
     *
     * @param storeIdList 门店ID列表
     * @param limit       每个门店取的条数上限
     * @return 门店ID 到铺货商品列表的映射, 无则空映射
     */
    Map<Long, List<DistributionRandomInfo>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, Integer limit);
}
