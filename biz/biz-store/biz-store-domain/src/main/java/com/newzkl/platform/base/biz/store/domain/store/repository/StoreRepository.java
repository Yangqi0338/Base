package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.query.StoreQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreSearchRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;

import java.util.List;
import java.util.Map;

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

    /**
     * 按样式编码统计各样式使用门店数
     *
     * @param styleCodes 样式编码列表
     * @return 样式编码到使用门店数的映射, 永不为 null
     */
    Map<String, Integer> countStoreByStyleCodes(List<String> styleCodes);

    Store storeByChannelId(Long accountId);

    Page<StoreRes> storePage(StoreQuery storeQueryReq);

    void cancelModelShop();

    Page<StoreSearchRes> storeSearchPage(StoreQuery storeQueryReq);

    List<Store> getStoreList(Store store);
}
