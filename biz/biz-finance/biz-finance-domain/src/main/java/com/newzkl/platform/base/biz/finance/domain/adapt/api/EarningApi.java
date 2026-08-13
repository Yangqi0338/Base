package com.newzkl.platform.base.biz.finance.domain.adapt.api;

import com.newzkl.platform.base.biz.finance.model.event.SkuOrderWaitEarningVO;

/**
 * 分润域出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.finance.rpc.api.earning.EarningApi};
 * 分润域尚未迁入 Base, 当前 infra 实现留桩(deferred, 见 deferred-issues), 能力齐后补全</p>
 *
 * @author KC
 */
public interface EarningApi {

    /**
     * 结算SKU订单分润
     *
     * @param waitEarning SKU订单待分润数据
     */
    void settleEarning(SkuOrderWaitEarningVO waitEarning);

    /**
     * 更新分润记录的角色
     *
     * @param accountId 账户ID
     * @param newRoleId 升级后角色ID
     */
    void updateRecordRole(Long accountId, Long newRoleId);
}
