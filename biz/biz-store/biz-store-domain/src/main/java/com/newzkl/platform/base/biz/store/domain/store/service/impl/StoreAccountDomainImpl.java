package com.newzkl.platform.base.biz.store.domain.store.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.msg.StoreAccountPayMsg;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreAccount;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountCreateReq;
import com.newzkl.platform.base.biz.store.model.store.query.StoreAccountQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountRes;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreAccountRepository;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreAccountDomain;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 门店用户关系
 */
@Service
@lombok.RequiredArgsConstructor
public class StoreAccountDomainImpl implements StoreAccountDomain {

    private final StoreAccountRepository storeAccountRepository;

    @Override
    public void createStoreAccount(StoreAccountCreateReq req) {
        storeAccountRepository.createStoreAccount(TransferUtils.transfer(req, StoreAccount::new));
    }

    @Override
    public void updateStoreAccount(StoreAccountUpdateReq req) {
        storeAccountRepository.updateStoreAccount(TransferUtils.transfer(req, StoreAccount::new));
    }

    @Override
    public Page<StoreAccountRes> queryStoreAccountPage(StoreAccountQuery req) {
        if (CollUtil.isEmpty(req.getSortField())) {
            req.addDescSortField("id");
        }
        return storeAccountRepository.queryStoreAccountPage(req);
    }

    @Override
    public void storeAccountPayEvent(StoreAccountPayMsg storeAccountPayMsg) {
        storeAccountRepository.storeAccountPayEvent(storeAccountPayMsg);
    }

    @Override
    public Long getDefultStoreId(Long accountId) {
        return storeAccountRepository.getDefultStoreId(accountId);
    }

    @Override
    public Map<Long, Integer> storeAccountCount(List<Long> storeIdList) {
        return storeAccountRepository.storeAccountCount(storeIdList);
    }
}
