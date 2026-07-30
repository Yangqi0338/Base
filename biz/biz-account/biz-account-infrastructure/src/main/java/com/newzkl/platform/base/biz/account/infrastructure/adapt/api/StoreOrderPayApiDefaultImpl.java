package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayRes;
import org.springframework.stereotype.Component;

/**
 * {@code StoreOrderPayApi} 默认兜底实现
 *
 * <p>infra-gap 清单:</p>
 * <ul>
 *   <li>下单支付: 能力在 biz-finance (旧 {@code IOrderPayApi#orderPay}), 跨服务链未接</li>
 *   <li>门店售价: 旧实现取字典 {@code CHANNEL_CONFIG.systemPrice}, 中台字典端口
 *       {@code DictApi} 已具备但门店价格结构未迁</li>
 *   <li>门店订单号: 旧实现用雪花号, 中台未提供等价生成器</li>
 * </ul>
 *
 * <p>影响: {@code POST /merchant/orderPay} 抛 {@code UnsupportedOperationException}, 契约已就位待接线。</p>
 *
 * @author KC
 */
@Component
public class StoreOrderPayApiDefaultImpl implements StoreOrderPayApi {

    @Override
    public StoreOrderPayRes orderPay(StoreOrderPayReq req) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 数智门店下单支付未迁 — 缺 biz-finance 下单支付端口接线 "
                        + "(旧 IOrderPayApi#orderPay)、字典 CHANNEL_CONFIG.systemPrice 门店价与雪花订单号生成");
    }
}
