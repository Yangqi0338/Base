package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;

/**
 * @author niu
 * @description: 客户账户配置仓库
 * @date 2024/4/3 10:14
 */
public interface AccountPurseConfigRepository {

    /**
     * 保存运营商杠杆配置
     *
     * @param accountId
     * @param radio
     */
    void saveOperatorLeverConfig(Long accountId, Integer radio);

    /**
     * 查询运营商杠杆配置
     *
     * @param operatorId
     * @return
     */
    Integer queryOperatorLever(Long operatorId);

    /**
     * 查询供应商配置/
     *
     * @return
     */
    ConfigSupplierVO querySupplierConfig();

    /**
     * 保存供应商配置
     *
     * @param configSupplierVO 供应商配置
     */
    void saveSupplierConfig(ConfigSupplierVO configSupplierVO);

    /**
     * 查询全局供应商配置
     */
    ChannelConfigVO defaultChannelConfig();

}
