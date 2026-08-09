package com.newzkl.platform.base.biz.store.domain.store.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.biz.store.model.enums.StoreStyleEnum;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStyleCreateReq;
import com.newzkl.platform.base.biz.store.model.store.query.StoreStyleQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStyleUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.req.SupplierTemplateUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleResponse;
import com.newzkl.platform.base.biz.store.model.store.res.SupplierTemplateRes;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreRepository;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreStyleRepository;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreStyleDomain;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 门店样式
 */
@Service
@lombok.RequiredArgsConstructor
public class StoreStyleDomainImpl implements StoreStyleDomain {

    private final StoreStyleRepository storeStyleRepository;

    private final StoreRepository storeRepository;

    @Override
    public Page<StoreStyleResponse> storeStylePage(StoreStyleQuery req) {
        return storeStyleRepository.storeStylePage(req);
    }

    @Override
    public void create(StoreStyleCreateReq req) {
        StoreStyle storeStyle = TransferUtils.transfer(req, StoreStyle::new);
        storeStyle.setCreateId(SecurityUtils.getAccountId());
        storeStyle.setCreateName(SecurityUtils.getNickName());
        storeStyle.setStyleCode(BusinessCodeUtil.generate(BusinessType.STORE_STYLE));
        storeStyleRepository.create(storeStyle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByCode(StoreStyleUpdateReq req) {
        if(Objects.equals(req.getType(), StoreStyleEnum.Type.DEFAULT.getCode())){
            storeStyleRepository.deleteDefaultStyle();

        }
        StoreStyle storeStyle = TransferUtils.transfer(req, StoreStyle::new);
        storeStyle.setMenderId(SecurityUtils.getAccountId());
        storeStyle.setMenderName(SecurityUtils.getNickName());
        storeStyleRepository.update(storeStyle);
    }

    @Override
    public List<SupplierTemplateRes> supplierTemplateList(Long accountId) {
        Store store = storeRepository.storeByChannelId(accountId);
        StoreStyle storeStyle = storeStyleRepository.getByStyleCode(store.getStyleCode());
        return Collections.singletonList(TransferUtils.transfer(storeStyle, SupplierTemplateRes::new));
    }

    @Override
    public void supplierTemplateUpdate(SupplierTemplateUpdateReq req) {
        StoreStyle storeStyle = TransferUtils.transfer(req, StoreStyle::new);
        storeStyleRepository.update(storeStyle);
    }

    @Override
    public StoreStyle getOneselfStyle(String storeStyle) {
        return storeStyleRepository.getOneselfStyle(storeStyle);
    }

    @Override
    public void deleteCopyStyle(String styleCode) {
        storeStyleRepository.deleteCopyStyle(styleCode);
    }

    @Override
    public StoreStyle copyStyle(String styleCode) {
        return storeStyleRepository.copyStyle(styleCode);
    }

    @Override
    public StoreStyle getByStyleCode(String styleCode) {
        return storeStyleRepository.getByStyleCode(styleCode);
    }
}
