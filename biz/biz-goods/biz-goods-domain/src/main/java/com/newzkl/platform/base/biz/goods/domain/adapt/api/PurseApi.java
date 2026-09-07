package com.newzkl.platform.base.biz.goods.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;

import java.util.List;

/**
 * 钱包域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.finance.rpc.api.purse.IAccountPurseApi}。
 * 席位套餐纵切迁入 biz-goods 时随迁 (唯一消费者为 {@code SeatPackageServiceImpl})</p>
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
