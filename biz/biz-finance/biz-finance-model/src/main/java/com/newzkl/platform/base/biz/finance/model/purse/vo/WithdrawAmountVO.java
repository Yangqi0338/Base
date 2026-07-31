package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 提现记录vo
 * @date 2023/12/27 15:28
 */
@Data
public class WithdrawAmountVO {

    /** 角色 */
    private RoleEnum.CompanyRole role;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 金额
     */
    private Integer totalAmount;

    /**
     * 可提现金额
     */
    private Integer amount;

    /**
     * 冻结金额
     */
    private String blockAmount;

    /**
     * 提现配置
     */
    private WithdrawConfig config;
}
