package com.newzkl.platform.base.biz.store.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.biz.store.application.service.StoreService;
import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountBaseInfo;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelContactReq;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelStoreVO;
import com.newzkl.platform.base.biz.store.domain.adapt.api.DistributionApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.UserFollowApi;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreAccountDomain;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreDomain;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreStyleDomain;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopDomain;
import com.newzkl.platform.base.common.core.utils.properties.SysProperties;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;
import com.newzkl.platform.base.biz.store.model.template.entity.ModelShop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 门店应用服务实现。
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreDomain storeDomain;
    private final ModelShopDomain modelShopDomain;
    private final StoreStyleDomain storeStyleDomain;
    private final StoreAccountDomain storeAccountDomain;
    private final AccountApi accountApi;
    private final ChannelApi channelApi;
    private final UserFollowApi userFollowApi;
    private final DistributionApi distributionApi;

    @Override
    public StoreStyleRes getModelShopStyle() {
        // 1. 查询当前用户信息
        AccountBaseInfo accountInfo = accountApi.accountInfo(SecurityUtils.getAccountId());
        if (accountInfo == null) {
            return null;
        }

        Store store;
        Long defultStoreId = storeAccountDomain.getDefultStoreId(accountInfo.getId());
        if (defultStoreId != null) {
            store = storeDomain.store(defultStoreId);
        } else {
            // 查询默认门店样式 TODO
//            store = storeDomain.store(SysProperties.officialChannelId);
            store = new Store();
        }

        StoreStyleRes storeStyleVO = TransferUtils.transfer(store, StoreStyleRes::new);

        // 样板店
        if (store.getModelShopId() != null) {
            ModelShop modelShop = modelShopDomain.queryById(store.getModelShopId());
            storeStyleVO.setModelShopName(modelShop.getModelShopName());
            storeStyleVO.setModelDescription(modelShop.getModelDescription());
        }
        // 样式
        StoreStyle style = storeStyleDomain.getByStyleCode(store.getStyleCode());
        storeStyleVO.setGoodsIdListStr(style.getGoodsIdListStr());
        storeStyleVO.setStyleContent(style.getStyleContent());
        return storeStyleVO;
    }

    @Override
    public ChannelStoreVO channelVO(Long accountId) {
        return accountApi.channelStoreInfo(accountId);
    }

    @Override
    public void updateChannel(ChannelContactReq req) {
        channelApi.editContact(CollUtil.toList(req));
    }

    @Override
    public StoreRes store(Long storeId) {
        StoreRes storeVO = TransferUtils.transfer(storeDomain.store(storeId), StoreRes::new);
        if (storeVO != null) {
            storeVO.setFanNumber(userFollowApi.countFollower(storeId));
            storeVO.setStoreSaleNum(distributionApi.getStoreTotalSellNum(storeId));
        }
        return storeVO;
    }
}
