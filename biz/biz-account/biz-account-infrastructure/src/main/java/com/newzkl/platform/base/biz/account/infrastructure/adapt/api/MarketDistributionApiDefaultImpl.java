package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.DistributionRandomInfo;
import com.newzkl.platform.base.biz.account.domain.adapt.api.MarketDistributionApi;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@code MarketDistributionApi} 默认兜底实现
 *
 * <p>infra-gap 清单:</p>
 * <ul>
 *   <li>门店随机铺货商品: 能力在 biz-market (旧
 *       {@code IDistributionRpcFacade#queryRandomDistributionByStoreIdList}), 跨服务链未接, 默认空映射</li>
 * </ul>
 *
 * <p>影响: {@code GET /user/account/userHomePage} 的 {@code storeInfo.goodsList} 为空,
 * 其余门店字段正常。</p>
 *
 * @author KC
 */
@Component
public class MarketDistributionApiDefaultImpl implements MarketDistributionApi {

    @Override
    public Map<Long, List<DistributionRandomInfo>> queryRandomDistributionByStoreIdList(List<Long> storeIdList, Integer limit) {
        // TODO[infra-gap]: biz-market 铺货随机查询跨服务未接
        return Collections.emptyMap();
    }
}
