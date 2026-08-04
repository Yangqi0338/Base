package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

/**
 * @author niu
 * @description: 添加客户账户req
 * @date 2023/12/18 15:12
 */
@Data
public class AddAccountPurseReq {
    /** 主键ID */
    private Long id;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 账户类型
     */
    private PurseEnum.PurseType purseType;

    /**
     * 初始账户金额
     */
    private Money initAmount = Money.ZERO;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;
}
