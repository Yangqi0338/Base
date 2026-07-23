package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.biz.user.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级查询。
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelQuery extends BusinessPageQuery {

    /**
     * 角色类型
     */
    private RoleEnum.CompanyRole type;
    /**
     * 等级值
     */
    private Integer value;

}
