package com.newzkl.platform.base.biz.finance.model.earnings.vo;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * @author niu
 * @description: 累计分润统计
 * @date 2024/7/11 9:48
 */
@Data
public class TotalEarningVO {

    /**
     * 累计已分润金额
     */

    /**
     * 带分润金额
     */
    private Money waitEarning;
}
