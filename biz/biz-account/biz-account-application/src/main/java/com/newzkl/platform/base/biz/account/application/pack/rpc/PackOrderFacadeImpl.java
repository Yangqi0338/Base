package com.newzkl.platform.base.biz.account.application.pack.rpc;

import com.newzkl.platform.base.biz.account.domain.service.PackOrderDomain;
import com.newzkl.platform.base.biz.account.facade.PackOrderFacade;
import com.newzkl.platform.base.biz.account.facade.model.PackOrderFacadeDTO;
import com.newzkl.platform.base.biz.account.model.pack.res.PackOrderRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 入会礼包订单对外契约实现 (inbound provider)
 *
 * <p>{@link PackOrderFacade} 的 provider 侧实现, 落编排层。消费方 = biz-finance
 * 支付回调 (读订单要素 + 支付成功回写状态)。</p>
 *
 * @author KC
 */
@Component
@RequiredArgsConstructor
public class PackOrderFacadeImpl implements PackOrderFacade {

    private final PackOrderDomain packOrderDomain;

    @Override
    public PackOrderFacadeDTO packOrderVO(Long orderId) {
        PackOrderRes res = packOrderDomain.packOrderVO(orderId);
        return TransferUtils.transfer(res, PackOrderFacadeDTO::new);
    }

    @Override
    public void paySuccess(Long orderId) {
        packOrderDomain.paySuccess(orderId);
    }
}
