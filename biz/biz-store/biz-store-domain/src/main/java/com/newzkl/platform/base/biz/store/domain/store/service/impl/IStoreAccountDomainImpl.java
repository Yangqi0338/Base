package com.newzkl.platform.base.biz.store.domain.store.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.msg.StoreAccountPayMsg;
import com.newzkl.platform.base.biz.store.model.web.EventTrackingReq;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreAccount;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountQueryReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreAccountUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreAccountResponse;
import com.newzkl.platform.base.biz.store.domain.store.repository.IStoreAccountRepository;
import com.newzkl.platform.base.biz.store.domain.store.service.IStoreAccountDomain;
import com.zkl.scm.web.utils.TransferUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 门店用户关系
 */
@Service
public class IStoreAccountDomainImpl implements IStoreAccountDomain {

    @Resource
    private IStoreAccountRepository storeAccountRepository;

    @Override
    public void createStoreAccount(StoreAccountCreateReq req) {
        storeAccountRepository.createStoreAccount(TransferUtils.transfer(req, StoreAccount::new));
    }

    @Override
    public void updateStoreAccount(StoreAccountUpdateReq req) {
        storeAccountRepository.updateStoreAccount(TransferUtils.transfer(req, StoreAccount::new));
    }

    @Override
    public Page<StoreAccountResponse> queryStoreAccountPage(StoreAccountQueryReq req) {
        if (CollUtil.isEmpty(req.getSortField())) {
            req.addDescSortField("create_time");
        }
        return storeAccountRepository.queryStoreAccountPage(req);
    }

    @Override
    public void eventTracking(EventTrackingReq req) {
        DateTime now = DateUtil.date();
        StoreAccountQueryReq storeAccountQueryReq = new StoreAccountQueryReq();
        storeAccountQueryReq.resetQueryList();
        storeAccountQueryReq.setAccountId(req.getAccountId());
        storeAccountQueryReq.setChannelId(req.getStoreId());
//        storeAccountQueryReq.setViewTimeL(DateUtil.offset(now, DateField.MINUTE, 30).toString());
        List<StoreAccountResponse> records = storeAccountRepository.queryStoreAccountPage(storeAccountQueryReq).getRecords();
        if (CollUtil.isNotEmpty(records)) {
            records.forEach(record -> {
                record.setCountVisitNumber(record.getCountVisitNumber() + 1);
                record.setLastViewTime(now.toLocalDateTime());
                storeAccountRepository.updateStoreAccount(BeanUtil.copyProperties(record, StoreAccount.class));
            });

        }
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
