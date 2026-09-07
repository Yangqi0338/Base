package com.newzkl.platform.base.biz.finance.application.pay.service;

import com.newzkl.platform.base.biz.finance.model.purse.req.SupplierPurchaseGoodsSeatReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;

/**
 * 供应商购买商品位编排接口
 *
 * <p>对齐渠道商 {@code GoodsSeatChannelService}: 跨域取席位套餐 (biz-store)、读供应商配置
 * 校验最小量与单价、按支付方式走营销金即时结算或三方拉起支付, 落购买记录。</p>
 *
 * @author KC
 */
public interface GoodsSeatSupplierService {

    /**
     * 供应商购买商品位
     *
     * <p>指定套餐 id (非 0) 时按套餐数量与价格购买; 否则走自定义数量 (读供应商配置校验最小量与单价)。
     * 营销金支付即时结算, 三方支付返回汇付拉起结果并落待付款记录。</p>
     *
     * @param req 购买入参 (supplierId 由入口回填)
     * @return 支付结果 (营销金返 {@code BalancePayResult}, 三方返 {@code HuiFuPayRes})
     */
    PayBaseResult supplierPurchaseGoodsSeat(SupplierPurchaseGoodsSeatReq req);
}
