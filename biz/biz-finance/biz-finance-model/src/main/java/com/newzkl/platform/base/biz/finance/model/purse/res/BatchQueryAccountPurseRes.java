package com.newzkl.platform.base.biz.finance.model.purse.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newzkl.platform.base.common.core.model.money.Money;
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
     * 账户余额
     */
    @JsonProperty("earnings")
    private Money amount;
}
