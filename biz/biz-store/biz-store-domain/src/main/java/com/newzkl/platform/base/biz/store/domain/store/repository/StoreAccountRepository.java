package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.msg.StoreAccountPayMsg;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreAccount;
import com.newzkl.platform.base.biz.store.model.store.query.StoreAccountQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountRes;

import java.util.List;
import java.util.Map;

/**
 * 门店用户关系
 */
public interface StoreAccountRepository {

    Page<StoreAccountRes> queryStoreAccountPage(StoreAccountQuery req);

    void createStoreAccount(StoreAccount storeAccount);

    void updateStoreAccount(StoreAccount storeAccount);

    void storeAccountPayEvent(StoreAccountPayMsg storeAccountPayMsg);

    /**
     * 获取用户的默认门店
     * @param accountId
     * @return
     */
    Long getDefultStoreId(Long accountId);

    /**
     * 查询门店客户总数
     * key 门店ID value 门店客户总数
     * @param storeIdList
     * @return
     */
    Map<Long, Integer> storeAccountCount(List<Long> storeIdList);
}
