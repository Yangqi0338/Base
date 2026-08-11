package com.newzkl.platform.base.biz.finance.domain.adapt.api;

import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.ConfigWithdrawVO;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;

/**
 * 字典域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IDictFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface DictApi {

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

    /**
     * 查询默认提现配置
     *
     * @return
     */
    ConfigWithdrawVO defaultWithdrawConfig();

    /**
     * 更新提现配置
     *
     * @return
     */
    void alterWithdrawConfig(ConfigWithdrawVO incomeWithdraw);
}
