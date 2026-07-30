package com.newzkl.platform.base.biz.store.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.store.domain.adapt.api.DistributionApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.DistributionRandomInfo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@code DistributionApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 分销域(market)跨域链延迟, 入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
@Component
public class DistributionApiDefaultImpl implements DistributionApi {

    @Override
    public Map<Long, List<DistributionRandomInfo>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, int limit) {
        // TODO[cross-service]: 远程 market 随机分销查询, 默认空 map
        return Collections.emptyMap();
    }

    @Override
    public Integer getStoreTotalSellNum(Long storeId) {
        // TODO[cross-service]: 远程 market 门店销量查询, 默认 0
        return 0;
    }

    @Override
    public void batchCopyDistribution(Long sourceChannelId, Long targetAccountId, java.util.Set<Long> goodsIdList) {
        // TODO[cross-service]: 远程 market 批量复制分销, 默认空操作
    }
}
