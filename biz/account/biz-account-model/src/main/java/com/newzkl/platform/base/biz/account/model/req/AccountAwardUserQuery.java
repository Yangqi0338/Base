package com.newzkl.platform.base.biz.account.model.req;


import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountAwardUserQuery extends PageQuery {

    /**
     * 账号search
     */
    private String name;

}
