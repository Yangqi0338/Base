package com.newzkl.platform.base.biz.account.model.req;


import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 账号奖励用户查询入参
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountAwardUserQuery extends PageQuery {

    /**
     * 账号搜索关键字
     */
    private String name;

}
