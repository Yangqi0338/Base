package com.newzkl.platform.base.biz.account.model.auth.req;


import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.Data;

import java.util.List;

@Data
public class RolePageQuery extends BusinessPageQuery {

    /**
     * id
     */
    private Long id;

    /**
     * id集合
     */
    private List<Long> idList;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 系统id
     */
    private Long systemId;

}
