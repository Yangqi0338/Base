package com.newzkl.platform.base.biz.finance.model.earnings.req;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 批量查询客户贡献值
 * @date 2024/1/27 17:09
 */
@Data
public class AccountContributeRpcQuery {

    /**
     * 客户id集合
     */
    private List<Long> accountIds;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;
}
