package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.ChannelServiceAmountRes;
import com.newzkl.platform.base.common.ddd.facade.ChargeConfigChannelReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinanceConfigApi;
import com.newzkl.platform.base.biz.finance.facade.AccountConfigFacade;
import com.newzkl.platform.base.biz.finance.facade.model.ChannelConfigRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.ChannelNowServiceFeeRes;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * {@code FinanceConfigApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 资金域(finance)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class FinanceConfigApiImpl implements FinanceConfigApi {

    @DubboReference
    private AccountConfigFacade accountFinanceConfigFacade;

    @Override
    public void saveChannelChargeConfig(ChargeConfigChannelReq req) {
        // TODO[cross-service]: 远程 finance 保存渠道商充值配置, 默认空实现
    }

    @Override
    public void saveOperatorLeverConfig(Long accountId, Integer leverageRatio) {
        // TODO[cross-service]: 远程 finance 保存运营商杠杆配置, 默认空实现
    }

    @Override
    public ChannelServiceAmountRes queryChannelConfig(Long channelId) {
        ChannelConfigRes channelConfigRes = accountFinanceConfigFacade.queryChannelConfig(channelId);
        return TransferUtils.transfer(channelConfigRes, ChannelServiceAmountRes.class);
    }

    @Override
    public ChannelNowServiceFeeRes queryChannelNowServiceFee(Long channelId) {
        ChannelConfigRes channelConfigRes = accountFinanceConfigFacade.queryChannelConfig(channelId);
        return new ChannelNowServiceFeeRes(channelId, channelConfigRes.getPlatformNowValue(), channelConfigRes.getOperatorNowValue());
    }
}
