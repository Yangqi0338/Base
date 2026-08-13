package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountPurseConfigRepository;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.DictApi;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
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

    private final DictApi dictApi;
    private final AccountApi accountApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfigSupplierVO querySupplierConfig() {
        // 替代实体表，直接使用dict中转
        return dictApi.querySupplierConfig();
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
    public void saveSupplierConfig(ConfigSupplierVO configSupplierVO) {
        dictApi.saveSupplierConfig(configSupplierVO);
    }

    @Override
    public ChannelConfigVO defaultChannelConfig() {
        return dictApi.defaultChannelConfig();
    }
}
