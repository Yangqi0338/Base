package com.newzkl.platform.base.biz.store.domain.adapt.api;

import java.util.List;

/**
 * 钱包域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.finance.rpc.api.purse.IAccountPurseApi}。</p>
 *
 * @author KC
 */
public interface PurseApi {

    /**
     * 查询账户钱包额度
     *
     * @param req 钱包查询入参
     * @return 钱包额度列表, 无则空集合
     */
    List<PurseAmountRes> queryPurse(AccountPurseReq req);
}
