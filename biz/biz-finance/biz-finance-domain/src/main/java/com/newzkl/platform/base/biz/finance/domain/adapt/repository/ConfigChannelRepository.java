package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.newzkl.platform.base.biz.finance.model.account.req.BatchQueryConfigChannelQuery;
import com.newzkl.platform.base.biz.finance.model.account.res.BatchQueryConfigChannelRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigChannelVO;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/1/22 15:16
 */
public interface ConfigChannelRepository {

    /**
     * 保存渠道商配置
     *
     * @param channelConfig
     */
    void saveChannelConfig(ConfigChannelVO channelConfig);

    /**
     * 查询渠道商配置
     *
     * @param channelId
     * @return
     */
    ConfigChannelVO queryChannelConfig(Long channelId);

    /**
     * 查询渠道商配置
     *
     * @param channelIds
     * @return
     */
    List<ConfigChannelVO> queryChannelConfigs(List<Long> channelIds);


    /**
     * 更新渠道商服务费当前配置
     *
     * @param channelConfig
     */
    void alterChannelConfigNowValue(ConfigChannelVO channelConfig);

    /**
     * 批量查询渠道商服务费配置
     *
     * @param req
     * @return
     */
    List<BatchQueryConfigChannelRes> batchQueryChannelConfig(BatchQueryConfigChannelQuery req);


}
