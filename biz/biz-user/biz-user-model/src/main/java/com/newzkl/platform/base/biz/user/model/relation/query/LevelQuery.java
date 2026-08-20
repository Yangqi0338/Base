package com.newzkl.platform.base.biz.user.model.relation.query;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级查询
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelQuery extends BizPageQuery {

    /**
     * 角色类型
     */
    private AccountEnum.Identity type;
    /**
     * 等级值
     */
    private Integer value;

}
