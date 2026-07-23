package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.Data;

import java.util.List;

@Data
public class SubStructureReq extends BusinessPageQuery {
    /**
     * 范围 0 仅直推
     */
    private Integer scope;
    /**
     * 父id
     */
    private Long pid;
    /**
     * 父id列表(查询优化)
     */
    private String pidList;

    /**
     * 角色id列表
     */
    private List<Long> roleIdList;

    /**
     * 用户名
     */
    private String username;

    public void setRoleId(Long roleId) {
        this.roleIdList = doWrapperList(roleIdList, roleId);
    }
}
