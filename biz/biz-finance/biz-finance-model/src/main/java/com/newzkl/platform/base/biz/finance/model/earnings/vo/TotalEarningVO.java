package com.newzkl.platform.base.biz.finance.model.earnings.vo;

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
    private Integer totalEarning;

    /**
     * 带分润金额
     */
    private Integer waitEarning;
}
