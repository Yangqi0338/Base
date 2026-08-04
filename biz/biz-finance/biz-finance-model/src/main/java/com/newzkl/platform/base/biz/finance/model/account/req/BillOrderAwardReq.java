package com.newzkl.platform.base.biz.finance.model.account.req;


import cn.hutool.core.lang.Opt;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

@Data
public class BillOrderAwardReq extends BaseRes {

    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
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
     * 商品spuId
     */
    private Long spuId;
    /**
     * 订单数
     */
    private Integer orderCount;

    /**
     * 角色名称
     */
    public String getRoleName() {
        return Opt.ofNullable(role).map(RoleEnum.CompanyRole::getValue).orElse(null);
    }
}
