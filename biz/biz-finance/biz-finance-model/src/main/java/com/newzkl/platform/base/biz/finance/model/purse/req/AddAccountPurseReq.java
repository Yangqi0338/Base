package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;

/**
 * 添加客户账户请求
 *
 * @author niu
 * @date 2023/12/18 15:12
 */
@Data
public class AddAccountPurseReq extends BaseReq {
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
    private PurseEnum.Type purseType;

    /**
     * 初始账户金额
     */
    private Money initAmount = Money.ZERO;

    /**
     * 客户类型
     */
    private PurseEnum.User accountType;
}
