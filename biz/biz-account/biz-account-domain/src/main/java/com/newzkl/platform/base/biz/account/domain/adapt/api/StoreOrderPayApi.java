package com.newzkl.platform.base.biz.account.domain.adapt.api;

/**
 * 数智门店购买下单支付出站端口 (outbound port)
 *
 * <p>迁移: 原 {@code IRoleServiceImpl.orderPay} 直连 {@code com.zkl.scm.finance.rpc.api.IOrderPayApi},
 * 并额外依赖 {@code IDictFacade}(取 CHANNEL_CONFIG 门店价) 与雪花号生成。
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface StoreOrderPayApi {

    /**
     * 门店订单支付
     *
     * @param req 门店订单支付入参
     * @return 支付结果
     */
    StoreOrderPayRes orderPay(StoreOrderPayReq req);
}
