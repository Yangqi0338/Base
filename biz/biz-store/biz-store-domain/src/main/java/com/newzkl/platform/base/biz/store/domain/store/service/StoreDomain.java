package com.newzkl.platform.base.biz.store.domain.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.req.StoreQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreSaveReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreSearchRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;

import java.util.List;

/**
* 门店
* @author fang
*/
public interface StoreDomain {

    Long storeSave(StoreSaveReq storeSaveReq);
    int storeEdit(Long id, StoreSaveReq storeSaveReq);
    int storeDelete(List<Long> storeIdList);
    Store store(Long storeId);

    Page<StoreRes> storePage(StoreQuery storeQueryReq);

    void cancelModelShop();

    int storeUpdate(Store store);

    Store getStoreByChannelId(Long channelId);

    List<Store> storeList(List<Long> storeIdList);

    Page<StoreSearchRes> storeSearchPage(StoreQuery storeQueryReq);

    List<Store> getStoreList(Store store);
}
