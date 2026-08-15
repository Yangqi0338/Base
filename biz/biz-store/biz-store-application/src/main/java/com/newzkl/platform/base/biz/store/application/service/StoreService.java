package com.newzkl.platform.base.biz.store.application.service;

import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelContactReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreOrderPayReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreOrderPayRes;
import com.newzkl.platform.base.common.ddd.facade.ChannelStoreVO;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;

/**
 * 门店应用服务
 *
 * @author KC
 */
public interface StoreService {

    /**
     * 获取门店样式
     */
    StoreStyleRes getModelShopStyle();

    /**
     * 查询渠道门店信息
     */
    ChannelStoreVO channelVO(Long accountId);

    /**
     * 更新渠道联系人
     */
    void updateChannel(ChannelContactReq req);

    /**
     * 查询门店
     */
    StoreRes store(Long storeId);

    /**
     * 数智门店购买下单支付
     *
     * <p>迁移自旧 {@code IRoleService.orderPay}: 校验下单账号身份 (渠道商或会员),
     * 渠道商已开通门店时拒绝, 随后经资金域出站端口下单支付。</p>
     *
     * @param orderPay 门店订单支付入参

     * @return 支付结果
     */
    StoreOrderPayRes orderPay(StoreOrderPayReq orderPay);
}
