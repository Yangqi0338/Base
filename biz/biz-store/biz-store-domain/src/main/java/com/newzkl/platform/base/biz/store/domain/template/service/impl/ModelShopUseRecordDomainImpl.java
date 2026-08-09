package com.newzkl.platform.base.biz.store.domain.template.service.impl;

import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopUseRecordDTO;
import com.newzkl.platform.base.biz.store.domain.template.repository.ModelShopUseRecordRepository;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopUseRecordDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 样板店使用记录领域服务实现类
 */
@Service
@RequiredArgsConstructor
public class ModelShopUseRecordDomainImpl implements ModelShopUseRecordDomain {

    private final ModelShopUseRecordRepository modelShopUseRecordRepository;

    @Override
    public void create(ModelShopUseRecordDTO modelShopUseRecord) {
        modelShopUseRecordRepository.create(modelShopUseRecord);
    }
}