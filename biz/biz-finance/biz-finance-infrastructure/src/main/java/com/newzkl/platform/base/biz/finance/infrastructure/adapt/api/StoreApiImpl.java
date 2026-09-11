package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.StoreApi;
import com.newzkl.platform.base.biz.store.facade.model.StoreRegisterReq;
import com.newzkl.platform.base.biz.store.facade.StoreFacade;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import org.springframework.stereotype.Component;

/**
 * {@code AccountApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 账户域(user)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component("financeStoreApi")
public class StoreApiImpl implements StoreApi {

    @RpcReference
    private StoreFacade storeFacade;

    @Override
    public boolean openStore(Long accountId, Long storeType, String storeName, String address) {
        StoreRegisterReq storeRegisterReq = new StoreRegisterReq();
        storeRegisterReq.setChannelId(accountId);
        storeRegisterReq.setStoreType(storeType);
        storeRegisterReq.setStoreName(storeName);
        storeRegisterReq.setAddress(address);
        return storeFacade.openStore(storeRegisterReq);
    }
}
