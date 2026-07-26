package com.newzkl.platform.base.biz.finance.domain.account.service;

import com.newzkl.platform.base.biz.finance.model.account.req.BatchQueryConfigChannelQuery;
import com.newzkl.platform.base.biz.finance.model.account.req.ChargeConfigChannelReq;
import com.newzkl.platform.base.biz.finance.model.account.res.BatchQueryConfigChannelRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigChannelVO;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.biz.finance.model.support.ChannelConfigVO;

import java.util.List;

/**
 * @author niu
 * @description: 客户账户配置
 * @date 2024/4/3 10:08
 */
public interface AccountPurseConfigDomain {

    /**
     * 保存运营商杠杆配置
     *
     * @param accountId
     * @param config
     */
    void saveOperatorLeverConfig(Long accountId, Integer config);

    /**
     * 查询运营商杠杆配置
     *
     * @param operatorId
     * @return
     */
    Integer queryOperatorLever(Long operatorId);

    /**
     * 保存渠道商服务费配置
     *
     * @param req
     */
    void saveChannelChargeConfig(ChargeConfigChannelReq req);

    /**
     * 更新渠道商当前服务费配置
     *
     * @param channelId      渠道商id
     * @param rechargeAmount 充值金额
     */
    void alterChannelNowChargeConfig(Long channelId, Integer rechargeAmount);

    /**
     * 查询渠道商服务费配置
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
     * 批量查询渠道商服务费配置
     *
     * @param req
     * @return
     */
    List<BatchQueryConfigChannelRes> batchQueryChannelConfig(BatchQueryConfigChannelQuery req);

    /**
     * 查询供应商配置
     *
     * @return
     */
    ConfigSupplierVO querySupplierConfig();

    /**
     * 查询全局数智门店配置。
     *
     * <p>取字典 {@code DictEnum.Key.CHANNEL_CONFIG}, 字典缺省时返回金额阈值全 0 的兜底对象, 永不为 null。</p>
     *
     * @return 数智门店配置
     */
    ChannelConfigVO defaultChannelConfig();
}
