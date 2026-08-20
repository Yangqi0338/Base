package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;

import java.util.List;

/**
 * 资金域钱包出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.finance.rpc.facade.IPurseFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface PurseApi {

    /**
     * 初始化账户钱包
     * @param accountId 账号id
     */
    void initFinance(Long accountId, String username, PurseEnum.User user);

    /**
     * 查询账户钱包余额
     *
     * @param req 查询入参
     * @return 钱包结果集合, 恒非 null
     */
    List<PurseAmountRes> queryPurse(AccountPurseReq req);
}
