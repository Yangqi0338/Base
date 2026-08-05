package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseConfigRepository;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.finance.model.support.ChannelConfigVO;
import com.newzkl.platform.base.common.core.model.enums.RedisEnum;
import com.newzkl.platform.base.biz.finance.model.enums.user.DictEnum;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.OperatorApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niu
 * @description:
 * @date 2024/4/3 10:14
 */
@Repository
@RequiredArgsConstructor
public class AccountPurseConfigRepositoryImpl implements AccountPurseConfigRepository {

    private final DictApi dictFacade;

    private final OperatorApi operatorFacade;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOperatorLeverConfig(Long accountId, Integer radio) {
        operatorFacade.leverSave(accountId, radio);
        // 修改后清除缓存
        RedisUtil.hDel(RedisEnum.Key.CONFIG_CACHE_PREFIX.getCode(), String.valueOf(accountId));
    }

    @Override
    public Integer queryOperatorLever(Long operatorId) {
        Integer lever = RedisUtil.hGet(RedisEnum.Key.CONFIG_CACHE_PREFIX.getCode(), operatorId);
        if (lever == null) {
            lever = operatorFacade.getLever(operatorId);
            RedisUtil.hSet(RedisEnum.Key.CONFIG_CACHE_PREFIX.getCode(), String.valueOf(operatorId), String.valueOf(lever));
        }
        return lever;
    }

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

    /**
     * 保存供应商配置
     *
     * <p>源实现落 {@code config_supplier} 单行表, Base 侧供应商配置统一收敛到字典
     * {@code DictEnum.Key.SUPPLIER_CONFIG}, 与 {@link AccountPurseConfigRepositoryImpl#querySupplierConfig} 同源。</p>
     *
     * @param configSupplierVO 供应商配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
