package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.req.StoreQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreSearchRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;

import java.util.List;
import java.util.Set;

/**
* 门店
* @author fang
*/
public interface StoreRepository {

    Long storeSave(Store store);
    int storeEdit(Store store);
    int storeDelete(List<Long> storeIdList);
    Store store(Long storeId);
    List<Store> storeList(List<Long> storeIdList);

    Store storeByChannelId(Long accountId);

    Page<StoreRes> storePage(StoreQuery storeQueryReq);

    void cancelModelShop();

    Page<StoreSearchRes> storeSearchPage(StoreQuery storeQueryReq);

    List<Store> getStoreList(Store store);
}
