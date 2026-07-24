package com.newzkl.platform.base.biz.finance.model.account.req;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BillOrderAwardQuery extends BizPageQuery {

    /**
     * 角色
     */
    private PurseEnum.PurseType purseType;

    /**
     * 账户类型
     */
    private PurseEnum.FinanceUser accountType;
}
