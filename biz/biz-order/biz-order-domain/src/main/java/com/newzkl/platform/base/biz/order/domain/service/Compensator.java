// 文件路径: com/zkl/scm/sale/domain/order/service/impl/compensator/Compensator.java

package com.newzkl.platform.base.biz.order.domain.service;


import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRequest;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;

/**
 * 补偿策略接口
 */
public interface Compensator {

    /**
     * 获取该补偿器对应的平台类型
     * @return PlatformTypeEnum
     */
    PlatformTypeEnum getPlatformType();

    /**
     * 执行具体的补偿逻辑
     *
     * @param request 待补偿的订单请求记录
     */
    void executeCompensation(ThirdPartyOrderRequest request);
}