package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 批量查询客户账户
 * @date 2024/1/26 10:26
 */
@Data
public class BatchAccountPurseQuery {

    /**
     * id集合
     */
    private List<Long> accountId;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 收益统计口径
     * @ext 1 累计收益 2 当前可提现收益; 无对应枚举, 保留 Integer
     */
    private Integer totalOrNow;
}
