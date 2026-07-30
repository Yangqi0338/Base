package com.newzkl.platform.base.biz.account.domain.adapt.api;

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
     * 保存运营商杠杆配置
     *
     * @param accountId      运营商账户ID
     * @param leverageRatio  杠杆倍率
     */
    void saveOperatorLeverConfig(Long accountId, Integer leverageRatio);

    /**
     * 查询渠道商当前服务费
     *
     * @param channelId 渠道商ID
     * @return 服务费结果, 无则 null
     */
    ChannelServiceAmountRes queryChannelNowServiceFee(Long channelId);
}
