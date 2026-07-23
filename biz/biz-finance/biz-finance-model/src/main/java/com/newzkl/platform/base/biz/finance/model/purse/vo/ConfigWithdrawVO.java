package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;


/**
 * 运营商 收益提现 (IncomeWithdraw)展示类
 *
 * @author kc
 * @since 2025-09-24 11:07:45
 */
@Data
public class ConfigWithdrawVO extends BaseVO {

    /**
     * 收益设置 平台订单分润比例
     */
    private Integer orderEarningRatio;

    /**
     * 提现设置
     */
    private WithdrawConfig withdraw;

}

