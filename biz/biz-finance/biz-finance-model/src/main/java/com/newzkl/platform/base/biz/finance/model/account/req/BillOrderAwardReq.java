package com.newzkl.platform.base.biz.finance.model.account.req;


import cn.hutool.core.lang.Opt;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

/**
 * 订单奖励账单请求
 */
@Data
public class BillOrderAwardReq extends BaseRes {

    /**
     * 角色ID
     */
    private AccountEnum.Identity identity;
    /**
     * 账户ID
     */
    private Long accountId;
    /**
     * 账户名称(手机号)
     */
    private String username;
    /**
     * 账户类型
     */
    private PurseEnum.Type purseType;
    /**
     * 账户类型
     */
    private PurseEnum.User accountType;
    /**
     * 金额
     */
    private Money amount;
    /**
     * 商品spuId
     */
    private Long spuId;
    /**
     * 身份名称
     */
    public String getIdentityName() {
        return Opt.ofNullable(identity).map(AccountEnum.Identity::getValue).orElse(null);
    }
}
