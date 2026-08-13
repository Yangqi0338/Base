package com.newzkl.platform.base.biz.account.domain.adapt.api;
import com.newzkl.platform.base.common.ddd.facade.ChannelServiceAmountRes;
import com.newzkl.platform.base.common.ddd.facade.ChargeConfigChannelReq;

import com.newzkl.platform.base.common.ddd.facade.ChannelNowServiceFeeRes;

/**
 * 资金域账户配置出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.finance.rpc.facade.IAccountPurseConfigFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface FinanceConfigApi {

    /**
     * 保存渠道商充值服务费配置
     *
     * @param req 配置入参
     */
    void saveChannelChargeConfig(ChargeConfigChannelReq req);


    /**
     * 查询渠道商服务费配置
     *
     * @param channelId
     */
    ChannelServiceAmountRes queryChannelConfig(Long channelId);

    /**
     * 查询渠道商当前服务费
     *
     * @param channelId 渠道商ID
     * @return 服务费结果, 无则 null
     */
    ChannelNowServiceFeeRes queryChannelNowServiceFee(Long channelId);
}
