package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 转出申请请求对象
 * @date 2023/12/23 11:41
 */
@Data
public class RollOutApplyReq {

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 转出账户类型
     */
    private PurseEnum.PurseType purseType;

    /**
     * 转出金额
     */
    private Integer amount;

    /**
     * 手续费
     */
    private Integer handlingFee;

    /**
     * 三方用户id
     */
    private String tripartiteAccountId;

}
