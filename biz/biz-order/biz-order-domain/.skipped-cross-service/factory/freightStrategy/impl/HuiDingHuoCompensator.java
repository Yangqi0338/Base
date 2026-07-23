// 文件路径: com/zkl/scm/sale/domain/order/service/impl/compensator/HuiDingHuoCompensator.java

package com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zkl.scm.model.biz.req.huidinghuo.HuiDingHuoCreateOrderReq;
import com.zkl.scm.model.biz.res.huidinghuo.HuiDingHuoCreateOrderRes;
import com.newzkl.platform.base.biz.order.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.PlatformTypeEnum;
import com.zkl.scm.openapi.model.ThirdPartyOrderRequest;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.Compensator;
import com.zkl.scm.util.biz.HuiDingHuoApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sijiwang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HuiDingHuoCompensator implements Compensator {

//    @DubboReference
//    private ThirdPartyOrderFacade orderRepository;

    @Override
    public PlatformTypeEnum getPlatformType() {
        return PlatformTypeEnum.HUI_DING_HUO;
    }

    @Override
    public void executeCompensation(ThirdPartyOrderRequest request) {
        log.info("开始补偿惠订货订单: {}", request.getBizOrderNo());
        try {
            // 1. 反序列化请求参数 (假设是 HuiDingHuoCreateOrderReq)
             HuiDingHuoCreateOrderReq createOrderReq = JSONObject.parseObject(request.getRequestJson(), HuiDingHuoCreateOrderReq.class);
            
            // 2. 调用第三方API进行补偿
            HuiDingHuoCreateOrderRes order = HuiDingHuoApiUtils.createOrder(createOrderReq);

            // 3. 处理成功结果
            // request.setResponseJson(objectMapper.writeValueAsString(response));
            request.setResponseJson(JSONObject.toJSONString(order));
            request.setRequestStatus(CommonEnum.RequestStatusEnum.getByCode(order.getSuccess()));
            request.setErrorMessage(order.getMessage());
//            orderRepository.save(request);
            log.info("惠订货订单补偿 id: {} , 补偿结果: {}", request.getBizOrderNo(), order.getCode());
        } catch (Exception e) {
            log.error("惠订货订单补偿失败，调用API异常。订单号: {}", request.getBizOrderNo(), e);
        }
    }
}