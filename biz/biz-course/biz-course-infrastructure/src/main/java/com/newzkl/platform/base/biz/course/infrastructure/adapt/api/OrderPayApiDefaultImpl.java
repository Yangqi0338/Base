package com.newzkl.platform.base.biz.course.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.course.domain.adapt.api.OrderPayApi;
import com.newzkl.platform.base.biz.course.domain.adapt.api.OrderPayCommand;
import com.newzkl.platform.base.biz.course.domain.adapt.api.PayResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@code OrderPayApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 支付归 biz-finance, 端口待接线。未接线前返回 null,
 * 保证本域可独立编排与测试; 入口 starter 侧应以远程 consumer 覆盖此默认实现。
 * bean 名带 course 前缀防与他域同名兜底冲突。</p>
 *
 * @author KC
 */
@Slf4j
@Component("courseOrderPayApiDefaultImpl")
public class OrderPayApiDefaultImpl implements OrderPayApi {

    @Override
    public PayResultDTO orderPay(OrderPayCommand command) {
        log.warn("OrderPayApi 未接线, 课程支付返回 null, orderNo={}",
                command == null ? null : command.getOrderNo());
        return null;
    }
}
