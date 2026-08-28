package com.newzkl.platform.base.biz.auth.model.permission.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
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
public class PermissionQuery extends BizPageQuery {

    /** 所属端 (由 domain 从 token 注入, 端隔离) */
    private AccountEnum.Client client;

    private PermissionEnum.Type type;

    /** 角色编码 */
    private String code;
}
