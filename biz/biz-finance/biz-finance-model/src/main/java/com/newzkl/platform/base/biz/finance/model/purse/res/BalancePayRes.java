package com.newzkl.platform.base.biz.finance.model.purse.res;

import com.newzkl.platform.base.biz.finance.model.pay.res.PayBaseRes;
import lombok.Data;

/**
 * @author niu
 * @description: 余额支付返回
 * @date 2024/1/24 9:31
 */
@Data
public class BalancePayRes implements PayBaseRes {

    /**
     * 支付状态
     */
    private boolean payState;

    /**
     * 运营商支付状态
     */
    private boolean operatorPayState;

}
