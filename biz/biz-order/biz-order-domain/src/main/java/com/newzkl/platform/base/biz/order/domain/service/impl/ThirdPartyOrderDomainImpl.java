package com.newzkl.platform.base.biz.order.domain.service.impl;

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
     * 记录一次三方动作 追加式 requestJson/responseJson 已由调用方序列化 直接落库
     */
    @Override
    @Transactional
    public void recordAction(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String bizOrderNo, String interfaceName, String thirdOrderNo,
                             String requestJson, String responseJson, CommonEnum.RequestStatusEnum requestStatus, String errorMessage) {
        ThirdPartyOrderRecordDTO record = ThirdPartyOrderRecordDTO
            .init(platformType, bizOrderNo, interfaceName, requestJson, responseJson, requestStatus, errorMessage);
        record.setThirdOrderNo(thirdOrderNo);
        // 仅开发者通知失败才排补偿 下单失败重推会在供应商侧造重复单 补偿日志行排期会自我增殖
        if (CommonEnum.RequestStatusEnum.FAILED == requestStatus
                && ThirdPartyOrderEnum.Action.NOTIFY.equals(interfaceName)) {
            record.scheduleNextRetry();
        }
        orderRepository.saveRecord(record);
    }

    /**
     * 按 id 更新一条三方订单记录 委托 saveRecord 的 upsert 语义
     */
    @Override
    @Transactional
    public ThirdPartyOrderRecordDTO updateRecord(ThirdPartyOrderRecordDTO record) {
        return orderRepository.saveRecord(record);
    }
}
