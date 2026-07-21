package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.Data;

import java.util.List;

/**
 * 用户账号
 *
 * @author fang
 */
@Data
public class AccountParentQuery extends PageQuery {
    private Long id;

    /**
     * 0 直推 1 所有
     */
    private Integer scope;

    /**
     * 父id列表
     *
     */
    private String pidList;

    /**
     * 身份
     */
    private CommonEnum.Client client;

    /**
     * 角色id列表
     */
    private List<Long> roleIdList;

    public void setRoleId(Long roleId) {
        this.roleIdList = doWrapperList(roleIdList, roleId);
    }

}
