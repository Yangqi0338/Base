package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseConfigRepository;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.finance.model.support.ChannelConfigVO;
import com.newzkl.platform.base.biz.finance.model.enums.RedisEnum;
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

    @Override
    public ChannelConfigVO defaultChannelConfig() {
        String value = dictFacade.get(DictEnum.Key.CHANNEL_CONFIG.getCode());
        ChannelConfigVO channelConfigVO = JSONUtil.toBean(value, ChannelConfigVO.class);
        if (channelConfigVO == null) {
            channelConfigVO = new ChannelConfigVO();

        }
        return channelConfigVO;
    }
}
