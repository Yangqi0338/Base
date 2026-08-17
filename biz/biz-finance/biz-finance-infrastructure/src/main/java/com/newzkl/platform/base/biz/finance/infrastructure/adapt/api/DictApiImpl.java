package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.biz.sys.facade.IDictFacade;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * {@code DictApi} 的跨域实现
 *
 * <p>经 {@link IDictFacade} 调 biz-sys 字典能力。对等旧
 * {@code @DubboReference IDictFacade}: Base 当前为单体, facade 实现与本类同上下文,
 * 直接按接口注入即可; 将来拆服务时改为远程 consumer, 本类与领域层零改动。</p>
 *
 * <p>字典值为 JSON 字符串, 反序列化 / null 兜底由调用方仓储负责
 * (见 {@code AccountPurseConfigRepositoryImpl}/{@code WithdrawRepositoryImpl})。</p>
 *
 * @author KC
 */
@Component("financeDictApi")
@RequiredArgsConstructor
public class DictApiImpl implements DictApi {

    private final IDictFacade dictFacade;

    @Override
    public ConfigSupplierVO querySupplierConfig() {
        String value = dictFacade.get(DictEnum.Key.SUPPLIER_CONFIG.getCode());
        ConfigSupplierVO configSupplierVO = JSONUtil.toBean(value, ConfigSupplierVO.class);
        if (configSupplierVO == null) {
            configSupplierVO = new ConfigSupplierVO();
            configSupplierVO.setSkuSpaceFee(0);
            configSupplierVO.setDepositSettleSub(0);
            configSupplierVO.setWithdrawRate(5);
        }
        return configSupplierVO;
    }

    @Override
    public void saveSupplierConfig(ConfigSupplierVO configSupplierVO) {
        dictFacade.set(DictEnum.Key.SUPPLIER_CONFIG.getCode(), JSONUtil.toJsonStr(configSupplierVO));
    }

    @Override
    public ChannelConfigVO defaultChannelConfig() {
        String value = dictFacade.get(DictEnum.Key.CHANNEL_CONFIG.getCode());
        ChannelConfigVO channelConfigVO = JSONUtil.toBean(value, ChannelConfigVO.class);
        if (channelConfigVO == null) {
            // 字典未配置时给出 0 兜底: 调用方对金额阈值做拆箱比较, null 会直接 NPE
            channelConfigVO = new ChannelConfigVO();
            channelConfigVO.setMinimumRechargeAmount(Money.ZERO);
            channelConfigVO.setMinimumWithdrawalAmount(Money.ZERO);
            channelConfigVO.setMaximumDailyWithdrawalAmount(Money.ZERO);
            channelConfigVO.setWithdrawalFee(0);
        }
        return channelConfigVO;
    }

}
