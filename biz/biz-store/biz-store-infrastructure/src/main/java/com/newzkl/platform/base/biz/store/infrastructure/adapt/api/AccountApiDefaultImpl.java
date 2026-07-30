package com.newzkl.platform.base.biz.store.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountBaseInfo;
import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountGroupInfo;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelStoreVO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * {@code AccountApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 账户域(user)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component("storeAccountApiDefaultImpl")
public class AccountApiDefaultImpl implements AccountApi {

    @Override
    public List<Long> queryMember(String nickname) {
        // TODO[cross-service]: 远程 user 昵称查询, 默认空集合
        return Collections.emptyList();
    }

    @Override
    public List<AccountGroupInfo> queryMemberByAccountIdList(List<Long> accountIdList) {
        // TODO[cross-service]: 远程 user 批量会员查询, 默认空集合
        return Collections.emptyList();
    }

    @Override
    public AccountBaseInfo accountInfo(Long accountId) {
        // TODO[cross-service]: 远程 user 账户查询, 默认返回空
        return null;
    }

    @Override
    public ChannelStoreVO channelStoreInfo(Long accountId) {
        // TODO[cross-service]: 远程 user 渠道门店查询, 默认返回空
        return null;
    }
}
