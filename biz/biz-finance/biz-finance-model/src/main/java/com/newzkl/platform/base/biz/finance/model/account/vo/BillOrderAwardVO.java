package com.newzkl.platform.base.biz.finance.model.account.vo;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BillOrderAwardVO extends BaseRes {

    /**
     * 角色id
     */
    private RoleEnum.CompanyRole role;

    /**
     * 账号id
     */
    private Long accountId;

    /**
     * 用戶名即手机号
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 钱包类型
     */
    private PurseEnum.PurseType purseType;

    /**
     * 账户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 金额
     */
    private Money amount;

    /**
     * 订单数
     */

    /**
     * 创建日期
     */
    private LocalDate createDate;

}
