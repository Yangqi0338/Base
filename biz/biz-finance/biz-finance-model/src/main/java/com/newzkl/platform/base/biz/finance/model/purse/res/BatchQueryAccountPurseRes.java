package com.newzkl.platform.base.biz.finance.model.purse.res;

import lombok.Data;

/**
 * @author niu
 * @description:
 * @date 2024/1/26 10:42
 */
@Data
public class BatchQueryAccountPurseRes {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 累计收益
     */
    private Integer totalEarnings;

    /**
     * 可用收益
     */
    private Integer earnings;
}
