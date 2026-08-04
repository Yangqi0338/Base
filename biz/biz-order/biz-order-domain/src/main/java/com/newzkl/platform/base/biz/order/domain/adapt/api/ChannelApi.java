package com.newzkl.platform.base.biz.order.domain.adapt.api;


import com.newzkl.platform.base.biz.order.model.dto.ChannelDTO;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.common.ddd.facade.ChannelNowServiceFeeRes;

import java.util.List;

/**
 * 渠道商出站端口
 *
 * @author KC
 */
public interface ChannelApi {

    /**
     * 按条件查询渠道商列表
     */
    List<ChannelDTO> channelList(List<Long> idList, String channelName);

    /**
     * 查询渠道商分润配置
     *
     * @param channelId 渠道商账户ID
     * @return 分润配置, 无则 null
     */
    EarningsConfigRpcVO channelEarningsConfig(Long channelId);

    /**
     * 查询渠道商当前服务费
     *
     * @param channelId 渠道商账户ID
     * @return 当前服务费, 未接入外部实现时为 null
     */
    ChannelNowServiceFeeRes queryNowServiceFee(Long channelId);
}
