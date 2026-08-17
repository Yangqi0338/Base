package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.json.JSONUtil;

import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderDomain;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sijiwang
 */
@Slf4j
@Service
public class ThirdPartyOrderDomainImpl implements ThirdPartyOrderDomain {
    
    @Autowired
    private ThirdPartyOrderRepository orderRepository;
    
    /**
     * 创建并保存一个新的第三方订单请求记录
     */
    @Override
    @Transactional
    public ThirdPartyOrderRecordDTO createRecord(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String bizOrderNo,
                                                 String interfaceName, Object requestObject, Object responseObject, CommonEnum.RequestStatusEnum requestStatus,
                                                 String errorMessage) {

        ThirdPartyOrderRecordDTO existing = orderRepository.findRecordByBizOrderNo(bizOrderNo);
        if (existing != null) {
            throw new IllegalArgumentException("业务订单号 " + bizOrderNo + " 已存在");
        }
        String requestJson = JSONUtil.toJsonStr(requestObject);
        String responseJson = JSONUtil.toJsonStr(responseObject);
        ThirdPartyOrderRecordDTO requestRecord = ThirdPartyOrderRecordDTO
            .init(platformType, bizOrderNo, interfaceName, requestJson, responseJson, requestStatus, errorMessage);
        return orderRepository.saveRecord(requestRecord);
    }

    /**
     * 记录一次三方动作 追加式 requestJson/responseJson 已由调用方序列化 直接落库
     */
    @Override
    @Transactional
    public void recordAction(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String bizOrderNo, String interfaceName, String thirdOrderNo,
                             String requestJson, String responseJson, CommonEnum.RequestStatusEnum requestStatus, String errorMessage) {
        ThirdPartyOrderRecordDTO record = ThirdPartyOrderRecordDTO
            .init(platformType, bizOrderNo, interfaceName, requestJson, responseJson, requestStatus, errorMessage);
        record.setThirdOrderNo(thirdOrderNo);
        orderRepository.saveRecord(record);
    }
}