package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 用户收益VO
 *
 * @author fang
 */
@Data
public class AccountFinanceVO extends BaseRes {
    /**
     * 礼包收益 (Money, 落库 BIGINT 分)
     */
    private Money packIncome;
    /**
     * 商品消费分润 (Money, 落库 BIGINT 分)
     */
    private Money goodsIncome;
    /**
     * 商品待结算消费分润 (Money, 落库 BIGINT 分)
     */
    private Money goodsSettleIncome;
    /**
     * 分红奖金 (Money, 落库 BIGINT 分)
     */
    private Money dividendBonus;
    /**
     * 总收益 (Money, 落库 BIGINT 分)
     */
    private Money totalIncome;
}