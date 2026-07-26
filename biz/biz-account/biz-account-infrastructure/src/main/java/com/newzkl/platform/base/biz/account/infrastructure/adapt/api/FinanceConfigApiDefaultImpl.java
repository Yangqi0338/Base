package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.ChannelServiceAmountRes;
import com.newzkl.platform.base.biz.account.domain.adapt.api.ChargeConfigChannelReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinanceConfigApi;
import org.springframework.stereotype.Component;

/**
 * {@link FinanceConfigApi} 默认兜底实现。
 *
 * <p>TODO[cross-service]: 资金域(finance)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component
public class FinanceConfigApiDefaultImpl implements FinanceConfigApi {

    @Override
    public void saveChannelChargeConfig(ChargeConfigChannelReq req) {
        // TODO[cross-service]: 远程 finance 保存渠道商充值配置, 默认空实现
    }

    @Override
    public void saveOperatorLeverConfig(Long accountId, Integer leverageRatio) {
        // TODO[cross-service]: 远程 finance 保存运营商杠杆配置, 默认空实现
    }

    @Override
    public ChannelServiceAmountRes queryChannelNowServiceFee(Long channelId) {
        // TODO[cross-service]: 远程 finance 查询渠道商当前服务费, 默认 null
        return null;
    }
}
