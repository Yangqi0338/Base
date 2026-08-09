package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.ddd.model.vo.AccountInfoVO;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private AccountFacade accountFacade;

    @Override
    public AccountGroupVO account(CommonEnum.Client client, Long accountId) {

        return accountFacade.accountInfo(client, accountId);
    }

    @Override
    public UpIdRes upId(CommonEnum.Client client, Long accountId) {
        // TODO[cross-service]: 远程 user 上级链路查询, 默认返回空对象
        return null;
    }

    @Override
    public void addGoodsPoints(Long accountId, Integer goodsPoints) {
        // TODO[cross-service]: 远程 user 提货积分增加, 默认空操作
    }
}
