package com.newzkl.platform.base.biz.store.domain.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.req.StoreQueryReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreSaveReq;
import com.newzkl.platform.base.biz.store.model.store.vo.StoreSearchVO;
import com.newzkl.platform.base.biz.store.model.store.vo.StoreVO;

import java.util.List;

/**
* 门店
* @author fang
*/
public interface IStoreDomain {

    Long storeSave(StoreSaveReq storeSaveReq);
    int storeEdit(Long id, StoreSaveReq storeSaveReq);
    int storeDelete(List<Long> storeIdList);
    Store store(Long storeId);

    Page<StoreVO> storePage(StoreQueryReq storeQueryReq);

    void cancelModelShop();

    int storeUpdate(Store store);

    Store getStoreByChannelId(Long channelId);

    List<Store> storeList(List<Long> storeIdList);

    Page<StoreSearchVO> storeSearchPage(StoreQueryReq storeQueryReq);

    List<Store> getStoreList(Store store);
}
