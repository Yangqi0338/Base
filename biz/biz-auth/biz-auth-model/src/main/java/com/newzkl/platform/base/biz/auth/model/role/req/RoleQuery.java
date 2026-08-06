package com.newzkl.platform.base.biz.auth.model.role.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色分页查询
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQuery extends BizPageQuery {

    /** 关键字 */
    private String keyword;

    /** 角色编码 */
    private String code;
}
