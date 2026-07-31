package com.newzkl.platform.base.biz.auth.model.role.req;

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
    /** 主键ID */
    private Long id;
    /** ID列表 */
    private List<Long> idList;
    /**
     * 角色名称 (查询)
     */
    private String name;
}
