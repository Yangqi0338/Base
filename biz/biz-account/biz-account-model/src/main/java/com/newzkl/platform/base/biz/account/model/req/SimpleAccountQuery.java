package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

/**
 * 简单账号查询入参
 */
@Data
public class SimpleAccountQuery extends BizPageQuery {
    /**
     * 角色ID
     */
    private AccountEnum.Identity identity;
    /**
     * 用户名称
     */
    private String username;
}
