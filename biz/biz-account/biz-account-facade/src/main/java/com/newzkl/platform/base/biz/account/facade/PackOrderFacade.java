package com.newzkl.platform.base.biz.account.facade;

import com.newzkl.platform.base.biz.account.facade.model.PackOrderFacadeDTO;

/**
 * 入会礼包订单对外契约 (inbound provider)
 *
 * <p>入会礼包订单按业务概念归属 biz-user (slug 10 概念订正)。消费方 = biz-finance
 * 支付回调 (读订单要素 + 支付成功回写状态), 由入口 starter 侧远程 consumer 接线。</p>
 *
 * <p>本接口只使用自带 facade model 作出入参, 物理上不引用 biz-user-model。</p>
 *
 * @author KC
 */
public interface PackOrderFacade {

    /**
     * 按订单 ID 查询礼包订单信息
     *
     * @param orderId 订单 ID
     * @return 订单信息
     */
    PackOrderFacadeDTO packOrderVO(Long orderId);

    /**
     * 礼包订单支付成功 (待支付→待发货)
     *
     * @param orderId 订单 ID
     */
    void paySuccess(Long orderId);
}
