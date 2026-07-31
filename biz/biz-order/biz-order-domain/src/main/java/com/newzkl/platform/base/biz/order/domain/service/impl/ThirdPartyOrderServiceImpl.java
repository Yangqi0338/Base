package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderService;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRequest;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author sijiwang
 */
@Slf4j
@Service
public class ThirdPartyOrderServiceImpl implements ThirdPartyOrderService {
    
    @Autowired
    private ThirdPartyOrderRepository orderRepository;
    
    @Autowired
    private ObjectMapper objectMapper; // Jackson用于JSON序列化/反序列化
    
    /**
     * 创建并保存一个新的第三方订单请求记录
     */
    @Override
    @Transactional
    public <T> ThirdPartyOrderRequest createRequestRecord(PlatformTypeEnum platformType, String bizOrderNo,
                                                          String interfaceName, T requestObject, Object responseObject, CommonEnum.RequestStatusEnum requestStatus,
                                                          String errorMessage) {
        try {
            Optional<ThirdPartyOrderRequest> existing = orderRepository.findByBizOrderNo(bizOrderNo);
            if (existing.isPresent()) {
                throw new IllegalArgumentException("业务订单号 " + bizOrderNo + " 已存在");
            }
            String requestJson = objectMapper.writeValueAsString(requestObject);
            String responseJson = objectMapper.writeValueAsString(responseObject);
            ThirdPartyOrderRequest requestRecord = ThirdPartyOrderRequest
                .init(platformType, bizOrderNo, interfaceName, requestJson, responseJson, requestStatus, errorMessage);
            return orderRepository.save(requestRecord);
        }
        catch (JsonProcessingException e) {
            log.error("序列化请求对象失败", e);
            throw new RuntimeException("创建请求记录失败", e);
        }
    }
}