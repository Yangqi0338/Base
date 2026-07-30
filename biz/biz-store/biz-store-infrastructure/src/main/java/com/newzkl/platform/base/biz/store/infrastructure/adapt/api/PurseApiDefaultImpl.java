package com.newzkl.platform.base.biz.store.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountPurseReq;
import com.newzkl.platform.base.biz.store.domain.adapt.api.PurseApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.PurseAmountRes;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * {@code PurseApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 钱包域(finance)跨域链延迟, 入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
@Component
public class PurseApiDefaultImpl implements PurseApi {

    @Override
    public List<PurseAmountRes> queryPurse(AccountPurseReq req) {
        // TODO[cross-service]: 远程 finance 钱包查询, 默认空集合
        return Collections.emptyList();
    }
}
