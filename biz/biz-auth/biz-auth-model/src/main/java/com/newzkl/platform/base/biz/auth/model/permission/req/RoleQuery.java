package com.newzkl.platform.base.biz.auth.model.permission.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色分页查询
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQuery extends BizPageQuery {

    /**
     * 所属端
     */
    private AccountEnum.Client client;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 角色编码
     */
    private List<String> codeList;
}
