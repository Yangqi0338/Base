package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinancePurseApi;
import com.newzkl.platform.base.common.ddd.facade.InitFinanceReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PurseAmountRes;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@code FinancePurseApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 资金域(finance)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class FinancePurseApiDefaultImpl implements FinancePurseApi {

    @Override
    public void initFinance(InitFinanceReq req) {
        // TODO[cross-service]: 远程 finance 初始化钱包, 默认空实现
    }

    @Override
    public List<PurseAmountRes> queryPurse(AccountPurseReq req) {
        // TODO[cross-service]: 远程 finance 钱包查询, 默认空集合
        return List.of();
    }
}
