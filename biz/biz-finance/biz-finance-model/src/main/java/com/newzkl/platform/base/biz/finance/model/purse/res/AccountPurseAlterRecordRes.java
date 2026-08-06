package com.newzkl.platform.base.biz.finance.model.purse.res;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 账户变动记录vo
 * @date 2023/12/23 16:38
 */
@Data
public class AccountPurseAlterRecordRes extends BaseRes {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 账户类型
     */
    private PurseEnum.PurseType purseType;

    /**
     * 1 进账 2 出账 财务用户类型
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 账号修改类型  1 进账 2 出账
     *
     */
    private PurseEnum.PurseAlterType alterType;

    /**
     * 分润修改类型  1 进账 2 出账
     *
     */
    private EarningsEnum.PurseAlterTypeEnum earningAlterType;

    /**
     * 金额
     */
    private Money amount;

    /**
     * 关联记录id
     */
    private Long joinRecordId;

    /**
     * 备注
     */
    private String remark;

    public LocalDateTime getTime() {
        return createTime;
    }
}
