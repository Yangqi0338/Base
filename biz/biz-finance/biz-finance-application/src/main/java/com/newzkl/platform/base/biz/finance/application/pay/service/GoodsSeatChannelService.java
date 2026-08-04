package com.newzkl.platform.base.biz.finance.application.pay.service;

import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.PayBaseResult;
import com.newzkl.platform.base.biz.finance.model.purse.req.ChannelPurchaseGoodsSeatReq;

/**
 * 渠道商购买商品位编排接口
 *
 * <p>迁移自 new-scm {@code BalancePayApiImpl.channelPurchaseGoodsSeat}。跨域取套餐 (biz-store)、
 * 读渠道配置 (字典)、按支付方式走采购金即时结算或三方拉起支付, 落购买记录</p>
 *
 * @author KC
 */
public interface GoodsSeatChannelService {

    /**
     * 渠道商购买商品位
     *
     * <p>指定套餐 id (非 0) 时按套餐数量与价格购买; 否则走自定义数量 (读渠道配置校验最小量与单价)。
     * 采购金支付即时结算成功, 三方支付返回汇付拉起结果并落待付款记录</p>
     *
     * @param req 购买入参 (channelId 由入口回填)
     * @return 支付结果 (采购金返 {@code BalancePayResult}, 三方返 {@code HuiFuPayRes})
     */
    PayBaseResult channelPurchaseGoodsSeat(ChannelPurchaseGoodsSeatReq req);
}
