package com.newzkl.platform.base.biz.account.application.service;

import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayRes;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantCmd;

/**
 * 商户应用服务
 *
 * <p>迁移自旧 {@code IRoleService} 中的商户切片。旧 {@code IRoleService} 是巨型应用服务,
 * 中台化后按端点归属拆分, 商户端 {@code /merchant/**} 的编排落在本服务。</p>
 *
 * @author KC
 */
public interface MerchantService {

    /**
     * 数智门店购买下单支付
     *
     * <p>迁移自旧 {@code IRoleService.orderPay}: 校验下单账号身份 (渠道商或会员),
     * 渠道商已开通门店时拒绝, 随后经资金域出站端口下单支付。</p>
     *
     * @param orderPay 门店订单支付入参
     * @param accountId 下单账号ID
     * @return 支付结果
     */
    StoreOrderPayRes orderPay(MerchantCmd.OrderPay orderPay, Long accountId);
}
