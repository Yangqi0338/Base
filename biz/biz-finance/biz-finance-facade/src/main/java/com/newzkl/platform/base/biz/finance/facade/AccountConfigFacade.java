package com.newzkl.platform.base.biz.finance.facade;



import com.newzkl.platform.base.biz.finance.facade.model.ChannelConfigRes;
import com.newzkl.platform.base.common.ddd.facade.ChargeConfigChannelReq;

/**
 * @author niu
 * @description: 账户财务配置
 * @date 2024/1/22 14:49
 */
public interface AccountConfigFacade {

    /**
     * 查询渠道商服务费配置
     * @param channelId
     * @return
     */
    ChannelConfigRes queryChannelConfig(Long channelId);

    void saveChannelChargeConfig(ChargeConfigChannelReq req);
}
