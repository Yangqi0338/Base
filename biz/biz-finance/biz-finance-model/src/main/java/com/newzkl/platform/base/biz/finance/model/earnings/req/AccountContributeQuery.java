package com.newzkl.platform.base.biz.finance.model.earnings.req;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author niu
 * @description:
 * @date 2024/1/25 15:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountContributeQuery extends BizPageQuery {

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 个人id
     */
    private Long myAccountId;
}
