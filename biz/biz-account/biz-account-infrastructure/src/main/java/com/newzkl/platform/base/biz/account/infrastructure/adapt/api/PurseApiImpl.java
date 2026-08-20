package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.facade.PurseFacade;
import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PurseApi;
import com.newzkl.platform.base.biz.finance.facade.model.InitFinanceReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PurseAmountRes;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
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
@Component("accountPurseApi")
public class PurseApiImpl implements PurseApi {

    @RpcReference
    private PurseFacade purseFacade;

    @Override
    public void initFinance(Long accountId, String username, PurseEnum.User user) {
        InitFinanceReq req = new InitFinanceReq();
        req.setAccountId(accountId);
        req.setAccountName(username);
        req.setPurseUser(user);
        purseFacade.initFinance(req);
    }

    @Override
    public List<PurseAmountRes> queryPurse(AccountPurseReq req) {
        // TODO[cross-service]: 远程 finance 钱包查询, 默认空集合
        return List.of();
    }
}
