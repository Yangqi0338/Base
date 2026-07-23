package com.newzkl.platform.base.biz.store.domain.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.msg.StoreAccountPayMsg;
import com.newzkl.platform.base.biz.store.model.web.EventTrackingReq;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreAccount;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountResponse;

import java.util.List;
import java.util.Map;

/**
 * 门店用户关系
 */
public interface StoreAccountDomain {

    void createStoreAccount(StoreAccountCreateReq req);

    void updateStoreAccount(StoreAccountUpdateReq req);

    Page<StoreAccountResponse> queryStoreAccountPage(StoreAccountQuery req);

    void eventTracking(EventTrackingReq req);

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
