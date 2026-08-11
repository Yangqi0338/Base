package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.NotifyApi;
import com.newzkl.platform.base.biz.finance.model.event.PaySuccessEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MQUtil;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import org.springframework.stereotype.Service;

@Service()
public class NotifyApiImpl implements NotifyApi {

    @Override
    public void paySuccess(Long orderId) {
        MQUtil.send(MQ.Tag.PAYMENT_PAY_SUCCESS, new PaySuccessEvent(orderId));
    }
}
