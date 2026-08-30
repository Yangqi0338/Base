package com.newzkl.platform.base.biz.finance.model.purse.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.newzkl.platform.base.common.core.model.money.Money;
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
     * 账户余额
     */
    @JsonProperty("earnings")
    private Money amount;

    /**
     * 开户时间
     */
    private LocalDateTime createTime;

}
