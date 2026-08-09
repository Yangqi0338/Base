package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工查询入参
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmpQuery extends BizPageQuery {

    /** 账号 */
    private String username;

    /** 账户ID */
    private Long accountId;
}
