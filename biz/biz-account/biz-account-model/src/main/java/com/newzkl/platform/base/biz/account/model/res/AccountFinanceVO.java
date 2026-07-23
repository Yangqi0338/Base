package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 用户收益VO
 *
 * @author fang
 */
@Data
public class AccountFinanceVO extends BaseVO {
    /**
     * 礼包收益
     */
    private Integer packIncome;
    /**
     * 商品消费分润
     */
    private Integer goodsIncome;
    /**
     * 商品待结算消费分润
     */
    private Integer goodsSettleIncome;
    /**
     * 分红奖金
     */
    private Integer dividendBonus;
    /**
     * 总收益
     */
    private Integer totalIncome;
}