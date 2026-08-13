package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.MessageApi;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MQUtil;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.ddd.facade.GoodsPaySuccessEvent;
import org.springframework.stereotype.Service;

/**
 * 消息发送出站端口实现
 *
 * <p>经 core-mq {@link MQUtil} 发送, domain 不感知具体 MQ 中间件</p>
 *
 * @author KC
 */
@Service
public class MessageApiImpl implements MessageApi {

    @Override
    public void sendGoodsPaySuccess(Long orderId) {
        GoodsPaySuccessEvent event = new GoodsPaySuccessEvent();
        event.setOrderId(orderId);
        MQUtil.send(MQ.Tag.GOODS_ORDER_PAY_SUCCESS, event);
    }
}
