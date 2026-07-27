package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.support.api.ChannelNowServiceFeeRes;

/**
 * 渠道商钱包配置出站端口
 *
 * @author KC
 */
public interface FinancePurseConfigApi {

    /**
     * 查询渠道商当前服务费
     *
     * @param channelId 渠道商账户ID
     * @return 当前服务费, 未接入外部实现时为 null
     */
    ChannelNowServiceFeeRes queryChannelNowServiceFee(Long channelId);
}
