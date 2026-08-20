package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.biz.account.facade.LevelFacade;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.common.ddd.facade.ChannelRegisterReq;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.ddd.facade.PermissionRpcVO;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import org.springframework.stereotype.Component;

/**
 * {@code AccountApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 账户域(user)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component("financeAccountApi")
public class AccountApiImpl implements AccountApi {

    @RpcReference
    private AccountFacade accountFacade;

    @RpcReference
    private LevelFacade levelFacade;

    @Override
    public AccountGroupVO account(AccountEnum.Client client, Long accountId) {
        return accountFacade.accountInfo(client, accountId);
    }

    @Override
    public UpIdRes upId(AccountEnum.Client client, Long accountId) {
        // TODO[cross-service]: 远程 user 上级链路查询, 默认返回空对象
        return null;
    }

    @Override
    public void addGoodsPoints(Long accountId, Integer goodsPoints) {
        // TODO[cross-service]: 远程 user 提货积分增加, 默认空操作
    }

    @Override
    public Integer limitAmount(Long accountId) {
        return 0;
    }

    @Override
    public void registerChannel(ChannelRegisterReq req) {
        accountFacade.registerChannel(req);
    }

    @Override
    public PermissionRpcVO levelPermissionVO(AccountEnum.Identity identity, Integer level) {
        return levelFacade.levelPermissionVO(identity, level);
    }
}
