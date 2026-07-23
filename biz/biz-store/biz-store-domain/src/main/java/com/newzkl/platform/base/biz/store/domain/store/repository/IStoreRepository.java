package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.req.StoreQueryReq;
import com.newzkl.platform.base.biz.store.model.store.vo.StoreSearchVO;
import com.newzkl.platform.base.biz.store.model.store.vo.StoreVO;

import java.util.List;
import java.util.Set;

/**
* 门店
* @author fang
*/
public interface IStoreRepository {

    Long storeSave(Store store);
    int storeEdit(Store store);
    int storeDelete(List<Long> storeIdList);
    Store store(Long storeId);
    List<Store> storeList(List<Long> storeIdList);

    Store storeByChannelId(Long accountId);

    Page<StoreVO> storePage(StoreQueryReq storeQueryReq);

    void cancelModelShop();

    Page<StoreSearchVO> storeSearchPage(StoreQueryReq storeQueryReq);

    List<Store> getStoreList(Store store);
}
