package com.newzkl.platform.base.biz.finance.model.purse.res;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PurseAmountRes implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 账户类型
     */
    private Integer purseType;

    /**
     * 客户类型
     */
    private Integer accountType;

    /**
     * 收益
     */
    private Integer earnings;

    /**
     * 总收益
     */
    private Integer totalEarnings;

    /**
     * 开户时间
     */
    private LocalDateTime createTime;

}
