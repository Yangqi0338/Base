package com.newzkl.platform.base.biz.store.application.service;

import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelContactReq;
import com.newzkl.platform.base.common.ddd.facade.ChannelStoreVO;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;

/**
 * 门店应用服务
 *
 * @author KC
 */
public interface StoreService {

    /**
     * 获取门店样式
     */
    StoreStyleRes getModelShopStyle();

    /**
     * 查询渠道门店信息
     */
    ChannelStoreVO channelVO(Long accountId);

    /**
     * 更新渠道联系人
     */
    void updateChannel(ChannelContactReq req);

    /**
     * 查询门店
     */
    StoreRes store(Long storeId);
}
