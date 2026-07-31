package com.newzkl.platform.base.biz.order.domain.adapt.api;



import com.newzkl.platform.base.biz.order.model.support.api.ChannelRes;

import java.util.List;

/**
 * 渠道商出站端口
 *
 * @author KC
 */
public interface AccountChannelApi {

    /**
     * 按条件查询渠道商列表
     *
     * @param query 渠道商查询入参
     * @return 渠道商列表, 恒非 null
     */
    List<ChannelRes> channelList(ChannelQuery query);
}
