package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopOrderRecordDTO;
import com.newzkl.platform.base.biz.store.model.template.res.ModeShopDataSummary;
import com.newzkl.platform.base.biz.store.model.template.vo.ModelShopOrderDataVO;
import com.newzkl.platform.base.biz.store.domain.template.repository.ModelShopOrderRecordRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.ModelShopOrderRecordDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ModelShopOrderRecordDO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 样板店订单记录仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class ModelShopOrderRecordRepositoryImpl implements ModelShopOrderRecordRepository {

    private final ModelShopOrderRecordDAO modelShopOrderRecordDAO;

    @Override
    public void create(ModelShopOrderRecordDTO modelShopOrderRecord) {
        modelShopOrderRecordDAO.insert(TransferUtils.transfer(modelShopOrderRecord, ModelShopOrderRecordDO::new));
    }

    @Override
    public List<ModelShopOrderDataVO> modelShopPayOrderData(Long modelShopId, List<Long> storeIdList) {
        return modelShopOrderRecordDAO.modelShopPayOrderData(modelShopId, storeIdList);
    }

    @Override
    public List<ModeShopDataSummary> countPayOrderByDay(Long modelShopId, LocalDateTime startTime, LocalDateTime endTime) {
        return modelShopOrderRecordDAO.countPayOrderByDay(modelShopId, startTime, endTime);
    }

    @Override
    public List<ModeShopDataSummary> sumPayAmountByDay(Long modelShopId, LocalDateTime startTime, LocalDateTime endTime) {
        return modelShopOrderRecordDAO.sumPayAmountByDay(modelShopId, startTime, endTime);
    }
}