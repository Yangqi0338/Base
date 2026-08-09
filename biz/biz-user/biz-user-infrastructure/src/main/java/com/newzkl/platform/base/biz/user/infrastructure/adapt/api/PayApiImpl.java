package com.newzkl.platform.base.biz.user.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.facade.PayFacade;
import com.newzkl.platform.base.biz.user.domain.adapt.api.PayApi;
import com.newzkl.platform.base.biz.user.domain.adapt.api.PayResultDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
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
@Component("userOrderPayApi")
public class PayApiImpl implements PayApi {

    @DubboReference
    private PayFacade payFacade;

    @Override
    public PayResultDTO packOrderPay(OrderPayReq req) {
        PayBaseResult payBaseResult = payFacade.orderPay(req);
        // TODO qrCode 如何返回
        return TransferUtils.transfer(payBaseResult, PayResultDTO.class);
    }
}
