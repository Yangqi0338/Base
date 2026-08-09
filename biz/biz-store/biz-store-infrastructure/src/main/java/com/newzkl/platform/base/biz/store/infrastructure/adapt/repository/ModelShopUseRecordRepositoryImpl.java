package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopUseRecordDTO;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopStorePageQuery;
import com.newzkl.platform.base.biz.store.model.template.res.ModeShopDataSummary;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStorePageRes;
import com.newzkl.platform.base.biz.store.domain.template.repository.ModelShopUseRecordRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.ModelShopUseRecordDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ModelShopUseRecordDO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 样板店使用记录仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class ModelShopUseRecordRepositoryImpl implements ModelShopUseRecordRepository {

    private final ModelShopUseRecordDAO modelShopUseRecordDAO;

    @Override
    public void create(ModelShopUseRecordDTO modelShopUseRecord) {
        modelShopUseRecordDAO.insert(TransferUtils.transfer(modelShopUseRecord, ModelShopUseRecordDO::new));
    }

    @Override
    public Page<ModelShopStorePageRes> modelShopStorePage(ModelShopStorePageQuery query) {
        return modelShopUseRecordDAO.selectLatestRecordsByGroup(com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport.page(query), query);
    }

    @Override
    public List<ModeShopDataSummary> countNewStoreByDay(Long modelShopId, LocalDateTime startTime, LocalDateTime endTime) {
        return modelShopUseRecordDAO.countNewStoreByDay(modelShopId, startTime, endTime);
    }
}