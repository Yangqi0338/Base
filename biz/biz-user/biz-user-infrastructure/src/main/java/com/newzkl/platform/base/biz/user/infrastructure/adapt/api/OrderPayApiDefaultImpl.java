package com.newzkl.platform.base.biz.user.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.user.domain.adapt.api.OrderPayApi;
import com.newzkl.platform.base.biz.user.domain.adapt.api.OrderPayCommand;
import com.newzkl.platform.base.biz.user.domain.adapt.api.PayResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@code OrderPayApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 支付归 biz-finance，端口待接线。未接线前返回 null，
 * 保证本域可独立编排与测试；入口 starter 侧应以远程 consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Slf4j
@Component("userOrderPayApiDefaultImpl")
public class OrderPayApiDefaultImpl implements OrderPayApi {

    @Override
    public PayResultDTO orderPay(OrderPayCommand command) {
        log.warn("OrderPayApi 未接线，礼包支付返回 null，orderNo={}", command == null ? null : command.getOrderNo());
        return null;
    }
}
