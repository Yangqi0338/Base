package com.newzkl.platform.base.biz.finance.model.purse.req;

import lombok.Data;

/**
 * 客户提现请求
 *
 * @author niu
 * @date 2023/12/25 15:26
 */
@Data
public class AccountWithdrawReq {

    /**
     * 提现金额
     */
    private Integer withdrawAmount;

    /**
     * 支付密码
     */
    private String password;

    /**
     * 密码随机因子
     */
    private String randomKey;
}
