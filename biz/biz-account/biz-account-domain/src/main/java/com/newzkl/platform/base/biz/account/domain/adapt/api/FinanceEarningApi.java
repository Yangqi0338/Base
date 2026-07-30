package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.biz.account.model.enums.finance.EarningsEnum;

import java.util.List;
import java.util.Map;

/**
 * 资金域收益出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.finance.rpc.facade.EarningFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface FinanceEarningApi {

    /**
     * 按消费类型汇总收益
     *
     * @param query 查询入参
     * @return 消费类型 -> 金额(分), 恒非 null
     */
    Map<EarningsEnum.ConsumeType, Integer> queryIncome(IncomeQuery query);

    /**
     * 查询账户收益贡献
     *
     * @param query 查询入参
     * @return 贡献结果集合, 恒非 null
     */
    List<EarningContributeRpcVO> queryEarningContribute(AccountContributeRpcQuery query);
}
