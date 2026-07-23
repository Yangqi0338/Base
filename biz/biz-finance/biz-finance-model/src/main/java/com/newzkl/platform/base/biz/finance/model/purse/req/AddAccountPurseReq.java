package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 添加客户账户req
 * @date 2023/12/18 15:12
 */
@Data
public class AddAccountPurseReq {
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
    private Integer initAmount = 0;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;
}
