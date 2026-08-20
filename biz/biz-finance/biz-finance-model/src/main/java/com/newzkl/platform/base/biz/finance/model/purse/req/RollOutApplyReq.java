package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * 转出申请请求
 *
 * @author niu
 * @date 2023/12/23 11:41
 */
@Data
public class RollOutApplyReq {

    /**
     * 客户类型
     */
    private PurseEnum.User accountType;

    /**
     * 转出账户类型
     */
    private PurseEnum.Type purseType;

    /**
     * 转出金额
     */
    private Money amount;

    /**
     * 手续费
     */
    private Money handlingFee;

    /**
     * 三方用户id
     */
    private String tripartiteAccountId;

}
