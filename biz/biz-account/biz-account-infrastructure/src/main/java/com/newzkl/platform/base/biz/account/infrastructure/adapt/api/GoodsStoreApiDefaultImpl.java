package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreAccountCreateReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreRPCVO;
import org.springframework.stereotype.Component;

/**
 * {@code GoodsStoreApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 商品域(goods)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class GoodsStoreApiDefaultImpl implements GoodsStoreApi {

    @Override
    public StoreRPCVO storeByChannelId(Long channelId) {
        // TODO[cross-service]: 远程 goods 按渠道商查门店, 默认 null
        return null;
    }

    @Override
    public void createStoreAccount(StoreAccountCreateReq req) {
        // TODO[cross-service]: 远程 goods 建立门店账号关联, 默认空实现
    }
}
