package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

/**
 * 后台角色
 *
 * @author fang
 */
@Data
public class AccountJobQuery extends BizPageQuery {

    private String name;
}
