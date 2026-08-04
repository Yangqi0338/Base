package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.AccountContributeRpcQuery;
import com.newzkl.platform.base.biz.account.domain.adapt.api.EarningContributeRpcVO;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinanceEarningApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.IncomeQuery;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * {@code FinanceEarningApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 资金域(finance)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class FinanceEarningApiDefaultImpl implements FinanceEarningApi {

    @Override
    public Map<EarningsEnum.ConsumeType, Integer> queryIncome(IncomeQuery query) {
        // TODO[cross-service]: 远程 finance 收益汇总, 默认空 Map
        return Map.of();
    }

    @Override
    public List<EarningContributeRpcVO> queryEarningContribute(AccountContributeRpcQuery query) {
        // TODO[cross-service]: 远程 finance 收益贡献查询, 默认空集合
        return List.of();
    }
}
