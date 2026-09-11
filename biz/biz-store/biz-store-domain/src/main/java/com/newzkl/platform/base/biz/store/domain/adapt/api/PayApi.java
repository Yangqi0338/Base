package com.newzkl.platform.base.biz.store.domain.adapt.api;

import com.newzkl.platform.base.biz.store.model.store.entity.ChannelVO;
import com.newzkl.platform.base.biz.store.model.store.req.StoreOrderPayRes;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;

import java.util.List;

/**
 * 渠道域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IChannelFacade}。</p>
 *
 * @author KC
 */
public interface PayApi {

    StoreOrderPayRes orderPay(OrderPayReq orderPayReq);
}
