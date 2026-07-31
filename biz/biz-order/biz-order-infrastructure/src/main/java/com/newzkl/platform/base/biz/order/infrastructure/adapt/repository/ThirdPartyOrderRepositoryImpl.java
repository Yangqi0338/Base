package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;



import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRequest;
import com.newzkl.platform.base.biz.order.infrastructure.dao.ThirdPartyOrderRequestDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.ThirdPartyOrderRequestDO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ThirdPartyOrderRepositoryImpl implements ThirdPartyOrderRepository {

    private final ThirdPartyOrderRequestDAO requestDAO;

    @Override
    public ThirdPartyOrderRequest save(ThirdPartyOrderRequest request) {
        ThirdPartyOrderRequestDO po = convertToPO(request);
        if (request.getId() == null) {
            requestDAO.insert(po);
            request.setId(po.getId());
        } else {
            requestDAO.updateById(po);
        }
        return request;
    }

    @Override
    public Optional<ThirdPartyOrderRequest> findByBizOrderNo(String bizOrderNo) {
        return requestDAO.selectByBizOrderNo(bizOrderNo).map(this::convertToDomain);
    }

    @Override
    public List<ThirdPartyOrderRequest> findByStatusAndNextRetryTimeBefore(PlatformTypeEnum platformType, CommonEnum.RequestStatusEnum status, String interfaceName) {
        List<ThirdPartyOrderRequestDO> poList = requestDAO.selectByStatusAndNextRetryTimeBefore(platformType.getCode(), status.getCode(), interfaceName);
        return poList.stream().map(this::convertToDomain).collect(Collectors.toList());
    }

    // --- 转换方法 ---
    private ThirdPartyOrderRequestDO convertToPO(ThirdPartyOrderRequest request) {
        ThirdPartyOrderRequestDO po = new ThirdPartyOrderRequestDO();
        po.setPlatformType(request.getPlatformType() != null ? request.getPlatformType().getCode() : null);
        po.setBizOrderNo(request.getBizOrderNo());
        po.setThirdOrderNo(request.getThirdOrderNo());
        po.setInterfaceName(request.getInterfaceName());
        po.setRequestJson(request.getRequestJson());
        po.setResponseJson(request.getResponseJson());
        po.setRequestStatus(request.getRequestStatus() != null ? request.getRequestStatus().getCode() : null);
        po.setErrorMessage(request.getErrorMessage());
        po.setRetryCount(request.getRetryCount());
        po.setNextRetryTime(request.getNextRetryTime());
        po.setCreatedAt(request.getCreatedAt());
        po.setUpdatedAt(request.getUpdatedAt());
        return po;
    }

    private ThirdPartyOrderRequest convertToDomain(ThirdPartyOrderRequestDO po) {
        ThirdPartyOrderRequest request = new ThirdPartyOrderRequest();
        request.setPlatformType(po.getPlatformType() != null ? PlatformTypeEnum.getByCode(po.getPlatformType()) : null);
        request.setBizOrderNo(po.getBizOrderNo());
        request.setThirdOrderNo(po.getThirdOrderNo());
        request.setInterfaceName(po.getInterfaceName());
        request.setRequestJson(po.getRequestJson());
        request.setResponseJson(po.getResponseJson());
        request.setRequestStatus(po.getRequestStatus() != null ? CommonEnum.RequestStatusEnum.getByCode(po.getRequestStatus()) : null);
        request.setErrorMessage(po.getErrorMessage());
        request.setRetryCount(po.getRetryCount());
        request.setNextRetryTime(po.getNextRetryTime());
        request.setCreatedAt(po.getCreatedAt());
        request.setUpdatedAt(po.getUpdatedAt());
        return request;
    }
}