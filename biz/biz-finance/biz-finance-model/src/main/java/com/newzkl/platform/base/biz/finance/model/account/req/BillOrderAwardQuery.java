package com.newzkl.platform.base.biz.finance.model.account.req;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单奖励账单查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BillOrderAwardQuery extends BizPageQuery {

    /**
     * 角色
     */
    private PurseEnum.Type purseType;

    /**
     * 账户类型
     */
    private PurseEnum.User accountType;
}
