package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.json.JSONUtil;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderService;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
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
    
    /**
     * 创建并保存一个新的第三方订单请求记录
     */
    @Override
    @Transactional
    public <T> ThirdPartyOrderRecordDTO createRecord(PlatformTypeEnum platformType, String bizOrderNo,
                                                     String interfaceName, T requestObject, Object responseObject, CommonEnum.RequestStatusEnum requestStatus,
                                                     String errorMessage) {

        ThirdPartyOrderRecordDTO existing = orderRepository.findRecordByBizOrderNo(bizOrderNo);
        if (existing == null) {
            throw new IllegalArgumentException("业务订单号 " + bizOrderNo + " 已存在");
        }
        String requestJson = JSONUtil.toJsonStr(requestObject);
        String responseJson = JSONUtil.toJsonStr(responseObject);
        ThirdPartyOrderRecordDTO requestRecord = ThirdPartyOrderRecordDTO
            .init(platformType, bizOrderNo, interfaceName, requestJson, responseJson, requestStatus, errorMessage);
        return orderRepository.saveRecord(requestRecord);
    }
}