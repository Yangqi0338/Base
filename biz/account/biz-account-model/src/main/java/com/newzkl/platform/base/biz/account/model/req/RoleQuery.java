package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * 角色
 *
 * @author fang
 */
@Data
public class RoleQuery extends PageQuery {
    private Long id;
    private List<Long> idList;
    /**
     * 角色名称 (查询)
     */
    private String name;
}
