package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountInfo;
import com.newzkl.platform.base.biz.finance.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import org.springframework.stereotype.Component;

/**
 * {@link AccountApi} 默认兜底实现。
 *
 * <p>TODO[cross-service]: 账户域(user)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class AccountApiDefaultImpl implements AccountApi {

    @Override
    public AccountInfo account(CommonEnum.Client client, Long accountId) {
        // TODO[cross-service]: 远程 user 账户查询, 默认返回空
        return null;
    }

    @Override
    public UpIdRes upId(CommonEnum.Client client, Long accountId) {
        // TODO[cross-service]: 远程 user 上级链路查询, 默认返回空对象
        return new UpIdRes();
    }

    @Override
    public void addGoodsPoints(Long accountId, Integer goodsPoints) {
        // TODO[cross-service]: 远程 user 提货积分增加, 默认空操作
    }
}
