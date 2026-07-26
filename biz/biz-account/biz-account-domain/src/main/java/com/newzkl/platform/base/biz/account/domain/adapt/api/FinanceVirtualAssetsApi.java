package com.newzkl.platform.base.biz.account.domain.adapt.api;

import java.util.List;

/**
 * 资金域虚拟资产 (期权) 出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.finance.rpc.api.IVirtualAssetsAlterApi};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface FinanceVirtualAssetsApi {

    /**
     * 批量变更虚拟资产 (期权)。
     *
     * @param reqList 变更入参列表
     */
    void alterVirtualAssets(List<VirtualAssetsAlterReq> reqList);
}
