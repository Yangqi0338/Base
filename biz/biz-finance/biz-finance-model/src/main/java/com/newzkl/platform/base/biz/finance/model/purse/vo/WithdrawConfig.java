package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;


/**
 * 提现设置
 *
 * @author kc
 * @since 2025-09-24 11:15:44
 */
@Data
public class WithdrawConfig implements Serializable {
    private static final long serialVersionUID = 506463511382728004L;
    /**
     * 最小提现金额
     */
    private Money minAmount;

    /**
     * 提现比例
     */
    private Double ratio;

    /**
     * 积分比例
     */
    private Double scoreRatio;

    /**
     * 提现手续费
     */
    private Money withdrawalFee;

    /**
     * 最大提现金额
     */
    private Money maxAmount;

    public WithdrawConfig init() {
        this.minAmount = Money.ZERO;
        this.ratio = 0.0;
        this.scoreRatio = 0.0;
        this.withdrawalFee = Money.ZERO;
        this.maxAmount = Money.ZERO;
        return this;
    }

}

