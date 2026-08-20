package com.newzkl.platform.base.biz.finance.model.purse.req.web;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * @author niu
 * @description: 转出申请请求对象
 * @date 2023/12/23 11:41
 */
@Data
public class RollOutApplyRequest {

    /**
     * 转出账户类型
     */
    @NotNull(message = "转出账户类型不能为空")
    private PurseEnum.Type purseType;

    /**
     * 转出金额
     */
    @NotNull(message = "转出金额不能为空")
    @Positive(message = "转出金额必须为正数")
    private Money amount;

}
